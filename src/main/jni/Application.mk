# Build both ARMv5TE and ARMv7-A machine code.
APP_ABI := armeabi armeabi-v7a

APP_MODULES = ultrasound-ogg ultrasound-flac ultrasound-native
APP_OPTIM = release

ULTRASOUND_NATIVE_FLAGS = \
	-Ijni/config \
	-Ijni/ogg/include \
	-DVERSION=\"1.2\" \
	-Ijni/flac/include \
	-Ijni/flac/src/libFLAC/include

APP_CFLAGS += $(ULTRASOUND_NATIVE_FLAGS)
APP_CXXFLAGS += $(ULTRASOUND_NATIVE_FLAGS)
