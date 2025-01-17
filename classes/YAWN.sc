YAWNShow {

	classvar <set, <inDict, <outDict, <kemperMIDI;
	classvar <sCheckBufs;
	classvar <headGroup, <tailGroup;
	classvar <verbBus, <verbSynth;

	*initClass {
		StartUp.add{

			SynthDef(\yawnReverb,{
				var sig = In.ar(\verbBus.kr(),2);
				sig = HPFSides.ar(sig,120);
				sig = sig + PinkNoise.ar(0.0000001!2);
				sig = FreeVerb.ar(sig,1,\room.kr(0.9),\damp.kr(0.99));
				// sig = ReplaceBadValues.ar(sig,post: 0);
				// sig = BHiShelf.ar(sig,2400,1,-9);
				sig = LPF.ar(sig,2500);
				Out.ar(\outBus.kr(),sig)
			}).add;
		}
	}

	*new { |setKey, inputs, outputs, kemperMIDIDevice, dmxBool = false, gui = 'openStageControl'|

		^super.new.init(setKey.asSymbol, inputs.asDict, outputs.asDict, kemperMIDIDevice, dmxBool, gui);
	}

	init { |setName, ins, outs, kemperMIDIDevice, dmxBool, controller|
		var server = Server.default;

		set = YAWNSet(setName);
		inDict = ins;
		outDict = outs;
		kemperMIDI = kemperMIDIDevice.asArray;
		sCheckBufs = IdentityDictionary();

		server.waitForBoot({
			var cond = CondVar();
			var mainPath = Platform.userExtensionDir +/+ "YAWN/";
			var sCheckSamples = mainPath +/+ "gui/sCheckSamples/";

			PathName(sCheckSamples).entries.do({ |entry|
				var key = entry.fileNameWithoutExtension.asSymbol;
				var buf = Buffer.read(server,entry.fullPath,action: { cond.signalOne });
				cond.wait { buf.numFrames.notNil };
				sCheckBufs.put(key, buf)
			});

			headGroup = Group(server);
			tailGroup = Group(server,'addToTail');

			verbBus = Bus.audio(server,2);

			if(dmxBool,{ DMXIS() });

			server.sync;

			if(kemperMIDIDevice.notNil,{ KemperMIDI(kemperMIDI[0],kemperMIDI[1]) }); // right???!?!?!

			server.sync;

			// launch gui
			thisProcess.interpreter.executeFile(mainPath +/+ "gui" +/+ setName +/+ "loadSetOSC.scd");

			server.sync;
			set.loadSet;

			server.sync;
			thisProcess.interpreter.executeFile(mainPath +/+ "gui/soundCheck.scd");

			server.sync;
			if(controller.notNil,{ this.launchOpenStageControl });

			server.sync;
			verbSynth = Synth(\yawnReverb,[\verbBus, verbBus,\outBus, outDict['processOut']], tailGroup);

		});
	}

	launchOpenStageControl {
		var unixString = "open /Applications/open-stage-control.app --args " ++
		"--send 127.0.0.1:% ".format(NetAddr.localAddr.port) ++
		// "--read-only " ++
		"--load '/Users/mikemccormick/Library/Application\ Support/SuperCollider/Extensions/YAWN/gui/main.json'";
		unixString.unixCmd; // returns pid, can use that to evenutally stop process on GUI close?
	}

	*cleanUp { } // what goes here?

}

/* ========================================== */

YAWNSet {

	classvar <setFolderPath;
	var <setKey, <setPath, <songs;

	*initClass {
		setFolderPath = Platform.userExtensionDir +/+ "YAWN/sets/";
	}

	*new { |setKey|
		^super.newCopyArgs(setKey).init
	}

	init {
		setPath = setFolderPath +/+ setKey;
		songs = PathName(setPath).entries.collect({ |folder|
			var songkey = folder.folderName.asSymbol;
			YAWNSong(songkey,setKey)
		});
	}

	songList {
		^songs.collect({ |song| song.songName }).postln;
	}

	loadSet {
		var cond = CondVar();

		fork{
			songs.do({ |song|
				song.loadData({ cond.signalOne });
				cond.wait { song.data.notNil }
			});
			"\nset: % LOADED".format(setKey).postln;
		}
	}

	*keys {
		PathName(setFolderPath).folders.do({ |folder|
			folder.folderName.postln
		});
	}
}

/* ========================================== */

YAWNSong {

	var <songName, <path, <pbTracks, <data;
	var pbTracksLoaded = false, synthDefsLoaded = false, oscDefsLoaded = false;

	*new { |name, setKey|
		^super.newCopyArgs(name).init(setKey);
	}

	init { |setKey|
		path = YAWNSet.setFolderPath +/+ setKey +/+ songName;
		pbTracks = IdentityDictionary();
	}

	loadData { |action|
		var dataPath = path +/+ "data.scd";
		var cond = CondVar();

		fork{
			this.loadPBtracks({ cond.signalOne });
			cond.wait { pbTracksLoaded };
			this.loadSynthDefs({ cond.signalOne });
			cond.wait { synthDefsLoaded };
			data = thisProcess.interpreter.executeFile(dataPath).value(this,{ cond.signalOne });
			cond.wait{ data.notNil };
			this.loadOSCDefs({ cond.signalOne });
			cond.wait { oscDefsLoaded };
			"% LOADED\n".format(songName).postln;
			action.value;
		};
	}

	loadPBtracks { |action, server|
		var cond = CondVar();
		server = server ? Server.default;
		fork {
			PathName(path +/+ "tracks").entries.do({ |entry|
				var key = entry.folderName.asSymbol;
				var folder = entry.entries.collect({ |track|
					var buf = Buffer.read(server,track.fullPath,action: { cond.signalOne });
					cond.wait {	buf.numFrames.notNil };
					buf
				});

				pbTracks.put(key,folder)

			});
			pbTracksLoaded = true;
			action.value;
		}
	}

	loadSynthDefs { |action|
		var filePath = path +/+ "synthDef.scd";
		var cond = CondVar();
		if(File.exists(filePath),{
			fork {
				thisProcess.interpreter.executeFile(filePath);
				synthDefsLoaded = true;
				"% synthDefs loaded".format(songName).postln;
				action.value
			}
		},{
			fork{
				synthDefsLoaded = true;
				"no % synthDefs to load".format(songName).postln;
				action.value
			}
		})
	}

	loadOSCDefs { |action|
		var filePath = path +/+ "osc.scd";
		var cond = CondVar();
		if(File.exists(filePath),{
			fork {
				thisProcess.interpreter.executeFile(filePath);
				oscDefsLoaded = true;
				"% oscDefs loaded".format(songName).postln;
				action.value
			}
		},{
			fork {
				oscDefsLoaded = true;
				"no % oscDefs to load".format(songName).postln;
				action.value
			}
		})
	}

	sections {
		if(data.isNil,{ this.loadData });             // maybe a warning/error here instead? or .fork this so that .loadData has enough time before the next line starts trying to access it..
		^data.collect({ |section| section['name'] });
	}

	clicks {
		if(data.isNil,{ this.loadData });                 // maybe a warning/error here instead? or .fork this so that .loadData has enough time before the next line starts trying to access it..
		^data.collect({ |section| section['click'] });
	}

	cueFrom { |from = 'intro', to = 'outro', click = true, lights = true, kemper = true, bTracks = true, countIn = false|
		var fromIndex = this.sections.indexOf(from);
		var toIndex = this.sections.indexOf(to);
		var countInArray = [], cuedArray = [];

		if( countIn,{
			if(data[fromIndex]['countIn'].flat.size > 0, {
				var count = data[fromIndex]['countIn'].deepCollect(2,{ |clk| clk.pattern });
				count = count.collect({ |clk| Pseq(clk) });
				countInArray = countInArray.add( Ppar( count ) );
			})
		});

		for(fromIndex,toIndex,{ |index|
			var sectionArray = [];

			if( click,{
				var clkArray = data[index]['click'].deepCollect(2,{ |clk| clk.pattern });

				clkArray = clkArray.collect({ |clk| Pseq(clk) });

				if(clkArray.size > 0,{
					sectionArray = sectionArray.add( Ppar( clkArray ) );
				});
			});

			if( lights,{
				var lightArray = data[index]['lights'];

				if(lightArray.size > 0,{
					sectionArray = sectionArray.add( Ppar( lightArray ) );
				});
			});

			if( kemper,{
				var kemperArray = data[index]['kemper'];

				if(kemperArray.size > 0,{
					sectionArray = sectionArray.add( Ppar( kemperArray ) );
				});
			});

			if( bTracks,{
				var trackArray = data[index]['bTracks'];

				if(trackArray.size > 0,{
					sectionArray = sectionArray.add( Ppar( trackArray ) );
				});
			});

			cuedArray = cuedArray.add( Ppar( sectionArray ) );
		});

		^Pdef("%_%|%|%|%|%|%".format(from, to, click, lights, kemper, bTracks, countIn).asSymbol,
			Pseq( countInArray ++ cuedArray )
		);
	}
}
