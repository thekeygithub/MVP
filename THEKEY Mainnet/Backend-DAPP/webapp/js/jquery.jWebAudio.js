/*
 * Copyright (C) 2013, Intel Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var jWebAudio = function() {
   if (window.hasOwnProperty('AudioContext')) {
        this.context = new AudioContext();
    } else if (window.hasOwnProperty('webkitAudioContext')) {
        this.context = new webkitAudioContext();
    } else {
        this.context = null;
        console.error('Web audio is not supported in current ' +
                'web browser. Please try with the latest' + 
                ' Chrome.');
    }
};
jWebAudio = new jWebAudio();

jWebAudio.SoundEngine = function() {
    this.soundArray = [];
};

jWebAudio.SoundEngine.prototype = {
    /* Create new Sound in `soundArray`.
     * `options`: {'url': ..., 'preLoad': ..., 'callback':..., 
     * 'multishot': ..., `finish`: ...}
     * Note that if there is already sound in current div,
     * it will be destroyed and a new one will be created.
     * `url`: file location of the sound
     * `preLoad`: load instantly if true, else will load when call `load`
     *            or `play`. Default false.
     * `callback`: function to be called after load, if preLoad. Note that if 
     *             url is an array, callback will be called when all sounds are
     *             loaded.
     * `multishot`: true if to allow play multi times with the same sound.
     *             Default false.
     * `finish`: function to be called when sound plays to the end, if is not
     *              multishot.
     * Returns sound object or array of objects accordingly
     */
    addSoundSource: function(options) {
        if (typeof options !== 'object' || !options.hasOwnProperty('url')) {
            console.error('Error url in addSoundSource.');
            return;
        }
        if (options.preLoad !== true) {
            options.preLoad = false;
        }
        if (typeof options.callback !== 'function' 
                && typeof options.callback !== 'object') {
            options.callback = null;
        }
        if (options.multishot !== true) {
            options.multishot = false;
        }
        if (typeof options.url === 'string') {
            this.soundArray.push(new jWebAudio.SoundSource(
                    this.soundArray.length, options));
            return this.soundArray[this.soundArray.length - 1];
            
        } else if (typeof options.url === 'object') {
            // Array of urls
            if (options.preLoad) {
                // Change callback, call callback when all are loaded.
                var totalCount = options.url.length;
                var count = 0;
                var realCallback = options.callback;
                options.callback = function() {
                    ++count;
                    if (count === totalCount) {
                        // last loaded
                        if (realCallback) {
                            realCallback();
                        }
                    }
                };
            }
            var start = this.soundArray.length;
            var urls = options.url.slice();
            for (var i in options.url) {
                options.url = urls[i];
                this.soundArray.push(new jWebAudio.SoundSource(
                        this.soundArray.length, options));
            }
            var returnArr = [];
            for (var i = start; i < this.soundArray.length; ++i) {
                returnArr[i - start] = this.soundArray[i];
            }
            return returnArr;
        }
    },
    
    /* Stops and destroys a sound in this element.
     * This function should be called when the sound is sure to be not 
     * used again.
     * `id`: id in jWebAudio.SoundEngine.soundArray
     */
    destroySound: function(id) {
        if (this.soundArray[id]) {
            if (this.soundArray[id].isLoaded) {
                this.soundArray[id].stop();
            }
            delete this.soundArray[id];
        }
    }
};



/* Constructor should not be called other than by jWebAudio.SoundEngine
 * `id`: id in jWebAudio.SoundEngine.soundArray
 * `options`: {'url': ..., 'preLoad': ..., 'callback':..., 'multishot': ...}
 * `url`: file location of the sound
 * `preLoad`: load instantly if true, else will load when call `load`
 *            or `play`. Default false.
 * `callback`: function to be called after loaded, only useful if preLoad
 * `multishot`: true if to allow play multi times with the same sound.
 *              Default false.
 * `finish`: function to be called when sound plays to the end, if is not
 *              multishot.
 * `options` also contain sound options like muted, loop...
 */
jWebAudio.SoundSource = function(id, options) {
    this.id = options.id;
    this.url = options.url;
    this.multishot = options.multishot;
    this.isLoaded = false;
    this.options = options; // used only to load
    this.finish = options.finish;
    
    var self = this;
    this.sound = null;
    
    if (options.preLoad === true) {
        this.load(options.callback);
    }
};

/* Load content of audio 
 * `callback`: function to be called after loaded.
 */
jWebAudio.SoundSource.prototype.load = function(callback) {
    if (!this.isLoaded) {
        var request = new XMLHttpRequest();
        request.open("GET", this.url, true);
        request.responseType = "arraybuffer";

        var self = this;
        request.onload = function() {
            jWebAudio.context.decodeAudioData(
                    request.response, function(buffer) {
                        if (self.multishot) {
                            self.sound = new jWebAudio.WebAudioMultishotSound(
                                    buffer);
                        } else {
                            self.sound = 
                                    new jWebAudio.WebAudioSound(
                                            buffer, self.finish);
                        }
                        self.sound.options = self.options;
                        self.isLoaded = true;
                        if (callback) {
                            callback();
                        }
                        return true;
                    }, function() {
                        console.error('Cannot decode: ' + self.url);
                        return false;
                    });
        };

        request.onerror = function() {
            console.error("Load error");
        };

        request.send();
    }
};



/*** Helper functions ***/
/* Make Child class extends Parent class */
jWebAudio.extend = function(Child, Parent) {
    var P = function(){};
    P.prototype = Parent.prototype;
    Child.prototype = new P();
    Child.prototype.constructor = Child;
};

/* Merge right into left with properties existing in left */
jWebAudio.leftMerge = function(left, right) {
    for (var i in right) {
        if (left.hasOwnProperty(i)) {
            if (typeof left[i] === 'object') {
                if (typeof right[i] === 'object') {
                    leftMerge(left[i], right[i]);
                } // ignore if right[i] is not object
            } else {
                left[i] = right[i];
            }
        }
    }
};



/* Basic control of sound */
jWebAudio.Sound = function() {
    // Play status
    this.STOPPED = 0;
    this.PLAYING = 1;
    this.PAUSED = 2;
    
    // Default fadeIn and fadeOut time
    var defaultFadeInTime = 3;
    var defaultFadeOutTime = 3;
    
    var self = this;
    // Default options for sound
    var options = {
        'loop': false,
        'loopGap': 0,
        'muted': false,
        'volume': 100,
        
        // fade in and out are not for multishot sound
        'fadeIn': false,
        'fadeOut': false,
        'fadeInTime': defaultFadeInTime,
        'fadeOutTime': defaultFadeOutTime
    };
    /* Get options */
    this.getOptions = function() {
        return options;
    };
    this.__defineGetter__('options', this.getOptions);
    /* Set options */
    this.setOptions = function(arg) {
        if (arg) {
            if (typeof arg.muted === 'boolean' && 
                    arg.muted !== options.muted) {
                self.setMuted(arg.muted);
            }
            if (typeof arg.volume === 'number' && 
                    arg.volume !== options.volume) {
                self.setVolume(arg.volume);
            }
            if (typeof arg.fadeIn === 'boolean') {
                options.fadeIn = arg.fadeIn;
                self.setFadeIn(options.fadeIn, arg.fadeInTime);
            }
            if (typeof arg.fadeOut === 'boolean') {
                options.fadeOut = arg.fadeOut;
                self.setFadeOut(options.fadeOut, arg.fadeOutTime);
            }
            // update only those with existing id
            jWebAudio.leftMerge(options, arg);
        }
    };
    this.__defineSetter__('options', this.setOptions);
    
    /* Set if to mute */
    this.setMuted = function(arg) {
        if (typeof arg !== 'boolean') {
            console.error('Error type of mute!');
            return;
        }
        options.muted = arg;
        if (arg) {
            gain.gain.value = 0;
        } else {
            gain.gain.value = options.volume / 100;
        }
    };
    this.getMuted = function(arg) {
        return self.options.muted;
    }

    /* Set volume */
    this.setVolume = function(arg) {
        arg = +arg;
        if (isNaN(arg)) {
            console.error('Error type of volume in setVolume');
            return;
        }
        options.volume = arg;
        gain.gain.value = arg / 100;
    };
    /* Get volume */
    this.getVolume = function() {
        return self.options.volume;
    };
    
    /* Get Loop */
    this.getLoop = function() {
        return self.options.loop;
    };
    
    var ctx = jWebAudio.context;
    
    var gain = ctx.createGainNode();
    this.__defineGetter__('gain', function() {
        return gain;
    });
    
    var effectArr = [];
    var effectName = [];

    /* Add sound effect */
    this.addEffect = function(effect) {
        if (typeof effect === 'string') {
            effect = new jWebAudio.Effect(effect);
        }
        if (!(effect instanceof jWebAudio.Effect)) {
            console.error('Error in addEffect: effect is not instance of Effect');
            return;
        }
        
        var first = firstEffect();
        var last = lastEffect();
        
        if (first !== null) {
            // Effect exists already
            // Disconnect last from dest
            last.outNode.disconnect();
            // Connect last to new effect
            last.outNode.connect(effect.inNode);
        } else {
            // First effect
            // Disconnect gain to dest
            gain.disconnect();
            // Connect gain to new effect
            gain.connect(effect.inNode);
        }
        // Connect new effect to dest
        effect.outNode.connect(ctx.destination);

        effectArr.push(effect);
        effectName.push(effect.getName());
        
        return effectArr.length - 1;
    };
    
    /* Get sound names */
    this.getEffectNames = function() {
        return effectName;
    };
    
    /* Get effect */
    this.getEffect = function(id) {
        return effectArr[id];
    };
    
    /* Delete effect with given index */
    this.deleteEffect = function(id) {
        if (effectArr[id] !== undefined) {
            // Find the nearest left effect
            var left = null;
            for (var i = id - 1; i >= 0; --i) {
                if (effectArr[i] !== undefined) {
                    left = effectArr[i].outNode;
                    break;
                }
            }
            // Find the nearst right effect
            var right = ctx.destination;
            for (var i = id + 1; i < effectArr.length; ++i) {
                if (effectArr[i] !== undefined) {
                    right = effectArr[i].inNode;
                    break;
                }
            }
            
            if (left === null) {
                // Connect to gain
                gain.disconnect();
                gain.connect(right);
            } else {
                // Connect to left
                left.disconnect();
                left.connect(right);
            }
            
            delete effectArr[id];
            delete effectName[id];
        }
    };

    /* Clear all effects */
    this.clearAllEffects = function() {
        for (var i = effectArr.length - 1; i >= 0; --i) {
            if (effectArr[i] !== undefined) {
                gain.disconnect();
                effectArr[i].outNode.disconnect();
                gain.connect(ctx.destination);

                effectArr = [];
                effectName = [];
                return;
            }
        }
    };
    
    /* Get left-most effect that is not undefined */
    function firstEffect() {
        for (var i = 0; i < effectArr.length; ++i) {
            if (effectArr[i] !== undefined) {
                return effectArr[i];
            }
        }
        return null;
    }
    
    /* Get right-most effect that is not undefined */
    function lastEffect() {
        for (var i = effectArr.length - 1; i >= 0; --i) {
            if (effectArr[i] !== undefined) {
                return effectArr[i];
            }
        }
        return null;
    }
    
    gain.connect(ctx.destination);
};

/* Web audio implementation of Sound class, extends Sound 
 * `finishFunc`: callback function to be called when sound is finished
 */
jWebAudio.WebAudioSound = function(buffer, finishFunc) {
    jWebAudio.Sound.call(this);
    
    var self = this; // for inner functions to call WebAudioSound
    
    var _ctx = jWebAudio.context;
    
    // Gain node for fading in and out
    var _fadeGain = _ctx.createGainNode();
    // Source connect _fadeGain, _fadeGain connect gain, 
    // gain connect sound effect gains
    _fadeGain.connect(self.gain);
    
    var _source = null;
    
    var _buffer = buffer;
    /* Get duration */
    this.__defineGetter__('duration', function() {
        return _buffer.duration;
    });
    
    var _offset = 0;
    /* Get offset */
    this.__defineGetter__('offset', function() {
        if (_state === self.PLAYING) {
            return (_ctx.currentTime - _startTime + _offset);
        }
        return _offset;
    });
    /* Seek position */
    this.seek = function(arg) {
        if (typeof arg !== 'number' || arg < 0 || arg > _buffer.duration) {
            console.error('Error arg in WebAudioSound.');
            return;
        }
        var wasPlaying = _state;
        stop();
        _offset = arg;
        // Play if was playing
        if (wasPlaying === this.PLAYING) {
            play();
        }
    };
    
    var _startTime = 0;
    
    var _state = this.STOPPED;
    /* Get state */
    this.__defineGetter__('state', function() {
        return _state;
    });
    
    var _finishEvent = null;
    var _fadeOutEvent = null;
    var _loopGapEvent = null;
    
    // Play if was not playing
    this.play = function() {
        if (_state === self.PLAYING) {
            return;
        }
        
        play();
    };
    // Play without checking previous state
    function play() {
        var duration = _buffer.duration - _offset;

        _source = _ctx.createBufferSource();
        _source.buffer = _buffer;
        _source.loop = self.options.loop;
        _source.connect(_fadeGain);
        
        // fade in
        if (self.options.fadeIn) {
            _fadeGain.gain.setValueAtTime(
                    _fadeGain.gain.minValue, _ctx.currentTime);
            _fadeGain.gain.linearRampToValueAtTime(_fadeGain.gain.maxValue, 
                    _ctx.currentTime + self.options.fadeInTime);
        }

        _source.noteGrainOn(0, _offset, duration);

        _startTime = _ctx.currentTime;
        _state = self.PLAYING;

        // Event fired when audio come to the end
        var finishTime = function() {
            return setTimeout(function() {
                if (self.options.loop === true) {
                    // start from beginning
                    self.seek(0);
                    // wait for loopGap to play
                    if (self.options.loopGap && self.options.loopGap > 0) {
                        self.stop();
                        loopGapEvent = setTimeout(function() {
                            play();
                        }, self.options.loopGap * 1000);
                    }
                } else {
                    _offset = 0;
                    _state = self.STOPPED;
                }
                
                if (finishFunc) {
                    // finish function defined
                    finishFunc();
                }
            }, duration * 1000);
        };
        _finishEvent = finishTime();
        
        // Fade out when play to the end
        if (self.options.fadeOut === true) {
            var fadeOutFunc = function() {
                return setTimeout(function() {
                    _fadeGain.gain.setValueAtTime(_fadeGain.gain.maxValue,
                            _ctx.currentTime);
                    _fadeGain.gain.linearRampToValueAtTime(_fadeGain.gain.minValue,
                            _ctx.currentTime + self.options.fadeOutTime);
                }, (duration - self.options.fadeOutTime) * 1000);
            };
            _fadeOutEvent = fadeOutFunc();
        }
    }

    this.pause = function() {
        if (_state !== self.PLAYING) {
            return;
        }
        
        stop();
        _state = self.PAUSED;
    };

    // Stop if is not stopped
    this.stop = function() {
        if (_state === self.STOPPED) {
            return;
        }

        function nodeOff() {
            stop();
            _offset = 0;
            _state = self.STOPPED;
        }
        
        // Fade out
        if (self.options.fadeOut) {
            var timeLeft = self.options.fadeOutTime;
            _fadeGain.gain.linearRampToValueAtTime(
                    _fadeGain.gain.minValue, _ctx.currentTime + timeLeft);
            // Stop source after faded out
            setTimeout(function() {
                nodeOff();
            }, timeLeft * 1000);
        } else {
            // Stop source immediately
            nodeOff();
        }
    };
    // Stop playing without checking
    // Note that is function makes the sound not playing, may either
    // caused by stopping or pausing the sound
    function stop() {
        if (_state === self.PLAYING) {
            _source.noteOff(0);
            _source = null;
        
            _offset += (_ctx.currentTime - _startTime);
        
            clearTimeout(_finishEvent);
            clearTimeout(_fadeOutEvent);
        }
    }
    
    /* Set fade in */
    this.setFadeIn = function(ifFadeIn, fadeInTime) {
        if (typeof ifFadeIn === 'boolean') {
            self.options.fadeIn = ifFadeIn;
            // Use fade time if defined, use default time if not
            if (typeof fadeInTime === 'number') {
                self.options.fadeInTime = fadeInTime;
            }
        } else {
            self.options.fadeOut = ifFadeOut;
            console.error('Error type in setFadeIn.');
        }
    };
    
    /* Set fade out */
    this.setFadeOut = function(ifFadeOut, fadeOutTime) {
        if (typeof ifFadeOut === 'boolean') {
            // Use fade time if defined, use default time if not
            if (typeof fadeOutTime === 'number') {
                self.options.fadeOutTime = fadeOutTime;
            }
        }
    };
};
// Extends Sound
jWebAudio.extend(jWebAudio.WebAudioSound, jWebAudio.Sound);


/* Web audio that allows to play under multi-shot, extends Sound */
jWebAudio.WebAudioMultishotSound = function(buffer) {
    jWebAudio.Sound.call(this);
    
    var _ctx = jWebAudio.context;

    var _buffer = buffer;
    var _playedSrc = [];
    
    var self = this;

    this.play = function() {
        var src = _ctx.createBufferSource();
        src.buffer = _buffer;
        src.loop = this.loop;

        src.connect(self.gain);
        src.noteOn(0);

        _playedSrc.push(src);
    };

    this.stop = function() {
        _playedSrc.forEach(function(element) {
            element.noteOff(0);
            element.disconnect();
        });

        _playedSrc = [];
    };
};
// Extends Sound
jWebAudio.extend(jWebAudio.WebAudioMultishotSound, jWebAudio.Sound);



/* Sound effects */
jWebAudio.Effect = function(name) {
    if (name === 'telephonize') {
        return new jWebAudio.Filter(name, [{
            'type': jWebAudio.Filter.prototype.LOWPASS,
            'frequency': 2000.0
        }, {
            'type': jWebAudio.Filter.prototype.HIGHPASS,
            'frequency': 500.0
        }]);
        
    } else if (name === 'cathedral') {
        return new jWebAudio.Filter(name, [{
            'type': jWebAudio.Filter.prototype.BANDPASS,
            'frequency': 3000.0
        }, {
            'type': jWebAudio.Filter.prototype.ALLPASS,
            'frequency': 1000.0
        }]);
        
    } else if (name === '3d') {
        return new jWebAudio.Spatiality();
    
    } else {
        console.error('Effect name: "' + name + '" not found');
    }
}



/* Sound filters, extends Effect */
jWebAudio.Filter = function(name, arg) {
    var effectName = name;
    this.getName = function() {
        return effectName;
    };
    
    var i, j, configArr = [], config, filter,
        _nodes = [];

    if (arg instanceof Array) {
        configArr = arg;
    } else if (arg instanceof Object) {
        configArr.push(arg);
    } else {
        return;
    }
    
    var ctx = jWebAudio.context;
    for (i = 0; i < configArr.length; ++i) {
        config = configArr[i];
        if (config.type >= 0 && config.type <= 7) {
            filter = ctx.createBiquadFilter();
            filter.type = config.type;
            filter.frequency.value = config.frequency;
            filter.Q.value = config.Q;
            filter.gain.value = config.gain;

            _nodes.push(filter);
        }
    }

    for (i = 0; i < _nodes.length - 1; ++i) {
        j = i + 1;
        _nodes[i].connect(_nodes[j]);
    }
    
    this.__defineGetter__('inNode', function() {
        return _nodes[0];
    });
            
    this.__defineGetter__('outNode', function() {
        return _nodes[_nodes.length - 1];
    });
}
// Filter extends Effect
jWebAudio.extend(jWebAudio.Filter, jWebAudio.Effect);

jWebAudio.Filter.prototype.LOWPASS = 0;
jWebAudio.Filter.prototype.HIGHPASS = 1;
jWebAudio.Filter.prototype.BANDPASS = 2;
jWebAudio.Filter.prototype.LOWSHELF = 3;
jWebAudio.Filter.prototype.HIGHSHELF = 4;
jWebAudio.Filter.prototype.PEAKING = 5;
jWebAudio.Filter.prototype.NOTCH = 6;
jWebAudio.Filter.prototype.ALLPASS = 7;



/* 3D sound, extends effect */
jWebAudio.Spatiality = function() {
    var ctx = jWebAudio.context;
    this.soundObject = ctx.createPanner();
    
    var self = this;
    this.__defineGetter__('inNode', function() {
        return self.soundObject;
    });
    
    this.__defineGetter__('outNode', function() {
        return self.soundObject;
    });
};
jWebAudio.extend(jWebAudio.Spatiality, jWebAudio.Effect);

jWebAudio.Spatiality.prototype.getName = function() {
    return '3d';
};

/*
 * Copyright (C) 2013, Intel Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Note that this file is only a wrapp of standard version.
 * You are sugguested to use the minified version named jquery.jWebAudio.min.js
 * under build fold.
 *
 * If you want to use this file for debugging, please make sure this file is 
 * included after standard.jWebAudio.js.
 */

(function ($) {
    // Make jWebAudio private in jQuery version so that user cannot call
    // jWebAudio outside library
    if (jWebAudio === undefined) {
        $.error('Please include standard.jWebAudio.js first!');
        return;
    }
    
    // private engine to add source
    var engine = new jWebAudio.SoundEngine();

    
    
    /*** Public jWebAudio functions for jQuery plugin ***/
    var methods = {
        /* Create new Sound in `soundArray`.
         * `options`: {'url': ..., 'preLoad': ..., 'callback':..., 
         * 'multishot': ...}
         * Note that if there is already sound in current div,
         * it will be destroyed and a new one will be created.
         * `url`: file location of the sound
         * `preLoad`: load instantly if true, else will load when call `load`
         *            or `play`. Default false.
         * `callback`: function to be called after load, if preLoad
         * `multishot`: true if to allow play multi times with the same sound.
         *              Default false.
         * `options` also contain sound options like muted, loop...
         */
        addSoundSource: function(options) {
            return this.each(function() {
                // only one sound source can exist in an element
                if (methods.existsSound.call($(this))) {
                    console.warn('Sound already exists. It will be '
                            + 'destroyed now.');
                    methods.destroySound.call($(this));
                }
                // url cannot be an array of string
                if (typeof options.url !== 'string') {
                    $.error('Error type of url!');
                    return;
                }
                
                // store sound id in element
                $(this).data('soundId', engine.soundArray.length);
                // add sound source
                engine.addSoundSource(options);
            });
        },
        
        /* Stops and destroys a sound in this element.
         * This function should be called when the sound is sure to be not 
         * used again.
         * `id`: the div id called jWebAudio
         */
        destroySound: function() {
            return this.each(function() {
                var id = $(this).data('soundId');
                engine.destroySound(id);
            });
        },
        
        /* Returns true if sound exists in current element, false otherwise */
        existsSound: function() {
            if ($(this).data('soundId') === undefined) {
                return false;
            } else {
                return true;
            }
        },
        
        /* Load content of audio  
         * `callback`: function to be called after loaded.
         */
        load: function(callback) {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var id = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                engine.soundArray[id].load(callback);
            });
        },
        
        /* Play audio */
        play: function() {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var id = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                engine.soundArray[id].sound.play();
            });
        },
        
        /* Pause if is not multishot audio.
         * Multishot audio can't be paused because they are designed to be 
         * simple short sound effects
         */
        pause: function() {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var id = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                engine.soundArray[id].sound.pause();
            });
        },
        
        /* Stop audio */
        stop: function() {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var id = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                engine.soundArray[id].sound.stop();
            });
        },
        
        /* Seek position */
        seek: function(position) {
            if ($(this).data('soundId') !== undefined) {
                // get id in sound source array
                var id = $(this).data('soundId');
            } else {
                // no sound source
                $.error('Please call addSoundSource first!');
                return;
            }
            var sound = engine.soundArray[id].sound;
            if (sound.options.multishot) {
                $.error('You cannot call seek with multishot sound!')
                return;
            }
            if (position === undefined) {
                // Get
                return sound.offset;
            } else {
                // Set
                return this.each(function() {
                    sound.seek(position);
                });
            }
        },
        
        /* Get duration (total length of the audio) */
        duration: function() {
            if ($(this).data('soundId') !== undefined) {
                // get id in sound source array
                var id = $(this).data('soundId');
            } else {
                // no sound source
                $.error('Please call addSoundSource first!');
                return;
            }
            var sound = engine.soundArray[id].sound;
            if (sound.options.multishot) {
                $.error('You cannot get duration with multishot sound!')
                return;
            }
            return sound.duration;
        },
        
        /* Set function to be called when sound plays to the end */
        finish: function(callback) {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var id = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                if (callback && typeof callback !== 'function') {
                    $.error('Error type of finish!');
                } else {
                    engine.soundArray[id].finish = callback;
                }
            });
        },
        
        /* Get effect array or 
         * add effect which returns the id in effect array
         * `effect`: could be name of effect, like 'telephonize'
         *           or object with `name` and `options` attribute
         *           to create new type of effect
         * `effect.name`: set the name of new effect
         * `effect.options`: set the options of new effect
         */
        addEffect: function(effect) {
            if ($(this).data('soundId') !== undefined) {
                // get id in sound source array
                var id = $(this).data('soundId');
            } else {
                // no sound source
                $.error('Please call addSoundSource first!');
                return;
            }
            var sound = engine.soundArray[id].sound;
            if (typeof effect === 'string') {
                // Use default effect
                return sound.addEffect(effect);
            } else if (typeof effect === 'object' && effect.name !== undefined
                    && effect.options !== undefined) {
                // Create new effect
                return sound.addEffect(new jWebAudio.Filter(
                        effect.name, effect.options));
            } else {
                $.error('Error type of effect!');
            }
        },
        
        /* Delete effect with given index in effect array
         * id can either be an int or an array of int
         */
        deleteEffect: function(id) {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var sid = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                var sound = engine.soundArray[sid].sound;
                if (typeof id === 'number') {
                    sound.deleteEffect(id);
                } else if (typeof id === 'object') {
                    for (var i in id) {
                        sound.deleteEffect(id[i]);
                    }
                }
            });
        },
        
        /* Get instance of 3D Effect */
        getEffect: function(id) {
            if ($(this).data('soundId') !== undefined) {
                // get id in sound source array
                var sid = $(this).data('soundId');
            } else {
                // no sound source
                $.error('Please call addSoundSource first!');
                return;
            }
            if (typeof id === 'number') {
                return engine.soundArray[sid].sound.getEffect(id);
            } else {
                $.error('Error type in set3dEffectPosition!');
            }
        },
        
        /* Clear all effects */
        clearAllEffects: function() {
            return this.each(function() {
                if ($(this).data('soundId') !== undefined) {
                    // get id in sound source array
                    var id = $(this).data('soundId');
                } else {
                    // no sound source
                    $.error('Please call addSoundSource first!');
                    return;
                }
                engine.soundArray[id].sound.clearAllEffects();
            });
        },
        
        /* Reset or get options of sound */
        options: function(opt) {
            if (opt === undefined) {
                // get options
                var id = $(this).data('soundId');
                return engine.soundArray[id].sound.options;
            } else {
                // set options
                return this.each(function() {
                    if ($(this).data('soundId') !== undefined) {
                        // get id in sound source array
                        var id = $(this).data('soundId');
                    } else {
                        // no sound source
                        $.error('Please call addSoundSource first!');
                        return;
                    }
                    engine.soundArray[id].sound.options = opt;
                });
            }
        }
    };
    
    
    
    /*** jQuery plugin facade ***/
    $.fn.jWebAudio = function() {
        var method = arguments[0];
        if (methods[method]) {
            // Call with given method
            if (arguments[1] === undefined) {
                // Get function
                return methods[method].call(this);
            } else {
                // Set function
                // Allow to set options or callback
                if (typeof arguments[1] === 'object') {
                    var options = $.extend({
                        callback: function() {}
                    }, arguments[1]);
                    if (typeof arguments[2] === 'function') {
                        $.extend(options, {
                            callback: arguments[2]
                        });
                    }
                } else {
                    var options = arguments[1];
                }
                return methods[method].call(this, options);
            }
        } else if (typeof method === 'object' || !method) {
            // Calls init if no method is given
            // Allow to set options or callback
            var options = $.extend({
                callback: function() {}
            }, arguments[0] || {});
            if (typeof arguments[1] === 'function') {
                $.extend(options, {
                    callback: arguments[1]
                });
            }
            return methods.init.call(this, options);
        } else {
            // Error if no method is matched
            $.error('Method ' + method + 
                    ' does not exist on jquery.jWebAudio');
        }
    };
    
})(jQuery);
