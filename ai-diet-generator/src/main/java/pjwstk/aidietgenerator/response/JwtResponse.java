package pjwstk.aidietgenerator.response;

import java.io.Serializable;

public class JwtResponse implements Serializable{

    private String jwtToken;

    public JwtResponse(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getToken(){
        return this.jwtToken;
    }

    @Override
    public String toString() {
        return jwtToken;
    }
}
