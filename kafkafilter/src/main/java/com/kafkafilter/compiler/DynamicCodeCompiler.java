package com.kafkafilter.compiler;

import java.io.File;

/**
 * @author Zongyang Li
 */
public interface DynamicCodeCompiler {
    Class compile(String sCode, String sName) throws Exception;

    Class compile(File file) throws Exception;
}