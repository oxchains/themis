<<<<<<< HEAD:BackEnd/themis-common/src/main/java/com/oxchains/common/model/RestResp.java
package com.oxchains.common.model;
=======
package com.oxchains.themis.common.model;
>>>>>>> b54ef991ebf23b343ec4f70ab27edc8e081f0b78:BackEnd/themis-common/src/main/java/com/oxchains/themis/common/model/RestResp.java

import java.io.Serializable;

/**
 * @author aiet
 */
public class RestResp implements Serializable{

    public final int status;
    public final String message;
    public final Object data;

    private RestResp(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private RestResp(int status, String messsage) {
        this(status, messsage, null);
    }

    public static RestResp success(Object data) {
        return new RestResp(1, "success", data);
    }

    public static RestResp success(String message, Object data){
        return new RestResp(1, message, data);
    }

    public static RestResp fail(String message, Object data){
        return new RestResp(-1, message, data);
    }

    public static RestResp fail(String message){
        return new RestResp(-1, message);
    }

    public static RestResp fail(){
        return new RestResp(-1, "fail");
    }

}
