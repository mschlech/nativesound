LOCAL_PATH := $(call my-dir)

LOCAL_C_INCLUDES += $(SBT_MANAGED_JNI_INCLUDE)



# flac statically
#
include $(CLEAR_VARS)

LOCAL_MODULE    := ultrasound-flac
LOCAL_SRC_FILES := \
	flac/src/libFLAC/bitmath.c \
	flac/src/libFLAC/bitreader.c \
	flac/src/libFLAC/cpu.c \
	flac/src/libFLAC/crc.c \
	flac/src/libFLAC/fixed.c \
	flac/src/libFLAC/float.c \
	flac/src/libFLAC/format.c \
	flac/src/libFLAC/lpc.c \
	flac/src/libFLAC/md5.c \
	flac/src/libFLAC/memory.c \
	flac/src/libFLAC/metadata_iterators.c \
	flac/src/libFLAC/metadata_object.c \
	flac/src/libFLAC/ogg_decoder_aspect.c \
	flac/src/libFLAC/ogg_encoder_aspect.c \
	flac/src/libFLAC/ogg_helper.c \
	flac/src/libFLAC/ogg_mapping.c \
	flac/src/libFLAC/stream_decoder.c \
	flac/src/libFLAC/stream_encoder.c \
	flac/src/libFLAC/stream_encoder_framing.c \
	flac/src/libFLAC/window.c \
	flac/src/libFLAC/bitwriter.c

include $(BUILD_STATIC_LIBRARY)



# ogg statically
#
include $(CLEAR_VARS)

LOCAL_MODULE    := ultrasound-ogg
LOCAL_SRC_FILES := \
	ogg/src/bitwise.c \
	ogg/src/framing.c

include $(BUILD_STATIC_LIBRARY)


LOCAL_MODULE    := ultrasound-native
LOCAL_MODULE_FILENAME:=ultrasound-native
### Add all source file names to be included in lib separated by a whitespace
LOCAL_SRC_FILES := AudioEncode.cpp \
                   AudioDecode.cpp \
                   util.cpp

LOCAL_LDLIBS := -llog

LOCAL_STATIC_LIBRARIES := ultrasound-ogg ultrasound-flac

include $(BUILD_SHARED_LIBRARY)
