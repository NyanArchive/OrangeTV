package tv.twitch.android.shared.player.network.manifest;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.manifest.extm3u;

public class extm3uParser {
    /* ... */

    public extm3u parse(String str) {
        extm3u extm3u = new extm3u();

        /* ... */

        String str21 = "PROXY-SERVER";
        String str22 = "server_name";
        String tmp1 = "";
        String tmp2 = "";

//        } else if (str21.equals("STREAM-TIME")) {
//            Double.parseDouble(str22);
//        }

        if (str21.equals("PROXY-SERVER")) { // TODO: __INJECT_CODE
            tmp1 = str22;
        }

        if (str21.equals("PROXY-URL")) { // TODO: __INJECT_CODE
            tmp1 = str22;
        }

        /* ... */

        extm3u.ProxyServer = tmp1; // TODO: __INJECT_CODE
        extm3u.ProxyUrl = tmp2; // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}