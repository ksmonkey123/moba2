package ch.awae.mobaproxy.lights;

import org.springframework.stereotype.Service;

@Service
public class LightButtonDecoder {

    public int decode(int input) {
        switch (input) {
            case 3:
                return 1;
            case 6:
                return 2;
            case 12:
                return 4;
            case 24:
                return 8;
            case 48:
                return 16;
            case 5:
                return 32;
            case 20:
                return 64;
            case 18:
                return 128;
            case 10:
                return 256;
            case 9:
                return 512;
            case 40:
                return 1024;
            case 33:
                return 2048;
            case 17:
                return 4096;
            case 36:
                return 8192;
            case 34:
                return 16384;
            default:
                return 0;
        }
    }
}
