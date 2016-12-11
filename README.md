# ZipMultipleFiles
Simple java utility with the purpouse to zip a certain amount of files via shell
USAGE
> java -jar ZipMultipleFiles.jar (WORKING_DIR) (FILES_NUMBER) (ZIP_NAME) [START_COUNTER_VALUE] [BUFFER_LENGTH] [SLEEP_TIME]

Where
* * args[0] = WORKING_DIR = the directory in which files are contained (mandatory)
* * args[1] = FILES_NUMBER = numbers of files per zip (mandatory)
* * args[2] = ZIP_NAME = output zip name (mandatory)
* * args[3] = START_COUNTER_VALUE => A counter value is added by default to the output zip name; default 0
* * args[4] = BUFFER_LENGTH => buffer size in order to create the zip file; default 16 KB buffer (16384)
* * args[5] = SLEEP_TIME => To reduce CPU consumption (default 1ms)
