package pjwstk.aidietgenerator.response;

import java.io.Serializable;

public class JwtResponse implements Serializable{

    private final String jwtToken;

    public JwtResponse(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getToken(){
        return this.jwtToken;
    }
}
