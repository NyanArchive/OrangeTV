package androidx.exifinterface.media;

import java.io.BufferedInputStream;
import java.io.IOException;

public class ExifInterface {
    private static boolean isJpegFormat(byte[] bArr) throws IOException {
        return false;
    }

    private boolean isRafFormat(byte[] bArr) throws IOException {
        return false;
    }

    private boolean isHeifFormat(byte[] bArr) throws IOException {
        return false;
    }

    private boolean isOrfFormat(byte[] bArr) throws IOException {
        return false;
    }

    private boolean isRw2Format(byte[] bArr) throws IOException {
        return false;
    }

    private boolean isPngFormat(byte[] bArr) throws IOException {
        return false;
    }

    private boolean isWebpFormat(byte[] bArr) throws IOException {
        return false;
    }

    private int getMimeType(BufferedInputStream bufferedInputStream) throws IOException { // TODO: __REPLACE_METHOD
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (isJpegFormat(bArr)) {
            return 4;
        }
        if (isPngFormat(bArr)) {
            return 13;
        }
        if (isWebpFormat(bArr)) {
            return 14;
        }
        if (isRafFormat(bArr)) {
            return 9;
        }
        if (isHeifFormat(bArr)) {
            return 12;
        }
        if (isOrfFormat(bArr)) {
            return 7;
        }
        if (isRw2Format(bArr)) {
            return 10;
        }
        return 0;
    }
}
