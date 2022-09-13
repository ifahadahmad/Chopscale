import os
from pathlib import Path
import shutil
import time


def main(inPath,outPath):
    try:
        shutil.rmtree(inPath)
    except OSError as e:
        print(e.strerror)
    
    seconds = time.time() - (2*60)

    if(os.path.exists(outPath)):
        for item in Path(outPath).glob("*"):
            if item.is_file():
                file_time = os.stat(os.path.join(outPath,item)).st_mtime
                if seconds >= file_time:
                    os.remove(os.path.join(outPath,item))