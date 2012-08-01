
#include "util.h"

#include <limits.h>
#include <string.h>
#include <stdarg.h>
#include <stdio.h>

namespace nativesound {
namespace jni {


/**
 * Convert a jstring to a UTF-8 char pointer. Ownership of the pointer goes
 * to the caller.
 **/
char * convert_jstring_path(JNIEnv * env, jstring input)
{
  char buf[PATH_MAX];

  jboolean copy = false;
  char const * str = env->GetStringUTFChars(input, &copy);
  if (NULL == str) {
    // OutOfMemoryError has already been thrown here.
    return NULL;
  }

  char * ret = strdup(str);
  env->ReleaseStringUTFChars(input, str);
  return ret;
}


void throwByName(JNIEnv * env, const char * name, const char * msg)
{
  jclass cls = env->FindClass(name);

  if (NULL != cls) {
    env->ThrowNew(cls, msg);

  }

  env->DeleteLocalRef(cls);
}


/**
 * Logger$ stuff printf-style
 **/
void log(int priority, char const * tag, char const * format, ...)
{
  va_list argptr;
  va_start(argptr, format);

  char line[4096];
  vsnprintf(line, sizeof(line), format, argptr);

  va_end(argptr);

  __android_log_write(priority, tag, line);
}


}}
