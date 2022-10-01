///////////////////////////////////////////////// YAWN Data: chaos /////////////////////////////////////////////////
{ |songInstance|
	// var cueKeys = [];
	var pathToDMXMIDI = songInstance.path +/+ "dmxMIDI/";
	var pathToKemperMIDI = songInstance.path +/+ "kemperMIDI/";

	var ambience = songInstance.pbTracks['ambience'];
	var bass     = songInstance.pbTracks['bass'];
	var gtrs     = songInstance.pbTracks['gtrs'];
	var perc     = songInstance.pbTracks['perc'];
	var synths   = songInstance.pbTracks['synths'];

	var outs = YAWNShow.outDict;

	[
		(
			name:    'cafeSaraIntro',
			countIn: [[ ClickConCat(1,Click(60/0.56836521053797,4),Click(60/0.56836521053797,4,2)).out_( outs['clickAll'] ) ]],
			click:   [[ Click(60/0.56836521053797,4,2,8,out: outs['clickAll']) ]],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraIntro', pathToKemperMIDI +/+ "intro.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraAplusBreak',
			countIn: [[ ]],                    // fix this
			click:   [
				[ ClickConCat(4,ClickEnv([145,100],4,2),ClickEnv([100,75],4,2),ClickEnv([75,120],4,2),ClickEnv([120,145],4,2)).out_( outs['clickOskar'] ),ClickCue(60/0.56836521053797,1,2, out: outs['clickOskar']) ],
				[ ClickConCat(4,ClickEnv([75,120],4),ClickEnv([120,145],4),ClickEnv([145,100],4,curve:1.01.neg),ClickEnv([100,75],4,curve: 2)).out_( outs['clickTorfinn'] ),ClickCue(60/0.56836521053797,1,2, out: outs['clickTorfinn']) ],
				[ Click(60/0.56836521053797,4,2,16,out: outs['clickMike']),ClickCue(60/0.56836521053797,1,2, out: outs['clickMike']) ]
			],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraAplusBreak',pathToKemperMIDI +/+ "AplusBreak.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraAagain',
			countIn: [[ ]],                    // fix this
			click:   [
				[ ClickConCat(2,ClickEnv([145,100],4,2),ClickEnv([100,75],4,2),ClickEnv([75,120],4,2),ClickEnv([120,145],4,2)).out_( outs['clickOskar'] ) ],
				[ ClickConCat(2,ClickEnv([75,120],4),ClickEnv([120,145],4),ClickEnv([145,100],4,curve:1.01.neg),ClickEnv([100,75],4,curve: 2)).out_( outs['clickTorfinn'] ) ],
				[ Click(60/0.56836521053797,4,2,8,out: outs['clickMike']) ]
			],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraAagain',pathToKemperMIDI +/+ "Aagain.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraCriff',
			countIn: [[ ]],                    // fix this
			click:   [
				[ ClickConCat(2,ClickEnv([145,100],4,2),ClickEnv([100,75],4,2),ClickEnv([75,120],4,2),ClickEnv([120,145],4,2)).out_( outs['clickOskar'] ) ],
				[ ClickConCat(2,ClickEnv([75,120],4),ClickEnv([120,145],4),ClickEnv([145,100],4,curve:1.01.neg),ClickEnv([100,75],4,curve: 2)).out_( outs['clickTorfinn'] ) ],
				[ Click(60/0.56836521053797,4,2,8,out: outs['clickMike']) ]
			],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraCriff', pathToKemperMIDI +/+ "Criff.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraAlast',
			countIn: [[ ]],                    // fix this
			click:   [
				[ ClickConCat(2,ClickEnv([145,100],4,2),ClickEnv([100,75],4,2),ClickEnv([75,120],4,2),ClickEnv([120,145],4,2)).out_( outs['clickOskar'] ) ],
				[ ClickConCat(2,ClickEnv([75,120],4),ClickEnv([120,145],4),ClickEnv([145,100],4,curve:1.01.neg),ClickEnv([100,75],4,curve: 2)).out_( outs['clickTorfinn'] ) ],
				[ Click(60/0.56836521053797,4,2,8,out: outs['clickMike']) ]
			],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraAlast', pathToKemperMIDI +/+ "Alast.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraBreak',
			countIn: [[ ]],                    // fix this
			click:   [[ Click(60/0.56836521053797,4,out: outs['clickAll']) ] ],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraBreak', pathToKemperMIDI +/+ "break.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraBsection',
			countIn: [[ ]],                    // fix this
			click:   [
				[ ClickConCat(6,ClickEnv([145,100],4,2),ClickEnv([100,75],4,2),ClickEnv([75,120],4,2),ClickEnv([120,145],4,2)).out_( outs['clickOskar'] ) ],
				[ ClickConCat(6,ClickEnv([75,120],4),ClickEnv([120,145],4),ClickEnv([145,100],4,curve:1.01.neg),ClickEnv([100,75],4,curve: 2)).out_([ outs['clickTorfinn'], outs['clickMike'] ]) ],
			],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraBsection', pathToKemperMIDI +/+ "Bsection.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraHarmony',
			countIn: [[ ClickConCat(1,Click(145,4),Click(145,1,2,4)).out_( outs['clickAll'] ) ]],                    // fix this
			click:   [[ ClickConCat(6,ClickEnv([145,100],4,2),ClickEnv([100,75],4,2),ClickEnv([75,120],4,2),ClickEnv([120,145],4,2)).out_( outs['clickAll'] ),ClickEnvCue([145,130],2,2,out: outs['clickAll']) ]],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraHarmony', pathToKemperMIDI +/+ "harmony.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraThrash',
			countIn: [[ ClickConCat(1,Click(90,2,repeats: 2),Click(90,1,repeats: 4)).out_( outs['clickAll'] ) ]],                    // fix this
			click:   [[ Click(90,4,repeats: 8,out: outs['clickAll'] ) ]],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraThrash',pathToKemperMIDI +/+ "thrash.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraRit',
			countIn: [[ ClickConCat(1,Click(90,2,repeats: 2),Click(90,1,repeats: 4)).out_( outs['clickAll'] ) ]],                    // fix this
			click:   [
				[ ClickConCat(1,Click(90,4,repeats: 2),ClickEnv([90,85],4),ClickEnv([85,76],3),Click(76,4,1,3),Click(76,3,1)).out_([ outs['clickTorfinn'], outs['clickMike'] ]) ],
				[ ClickConCat(1,Click(90,4,repeats: 2),ClickEnv([90,85],4),ClickEnv([85,76],3),Click(76,4,2,3),Click(76,3,2)).out_( outs['clickOskar'] ) ]
			],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraRit', pathToKemperMIDI +/+ "rit.mid") ],
			bTracks: [ ],
		),(
			name:    'cafeSaraOutro',
			countIn: [[ ClickConCat(1,Click(76,2,repeats: 2),Click(76,1,repeats: 4)).out_( outs['clickAll'] ) ]],                    // fix this
			click:   [[ ClickConCat(1,ClickEnv([75,120],4,2),ClickEnv([120,145],4,2),ClickEnv([145,100],4,2),ClickEnv([100,75],4,2)).out_( outs['clickAll'] ) ]],
			lights:  [ ],
			kemper:  [ KemperMIDI.makePat('cafeSaraOutro',pathToKemperMIDI +/+ "outro.mid") ],
			bTracks: [ ],
		)
	]
}