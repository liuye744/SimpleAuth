package com.codingcube.simpleauth.util;

import com.codingcube.simpleauth.auth.strategic.AuthRejectedStratagem;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.validated.strategic.ValidateRejectedStratagem;


public abstract class NullTarget implements
        ValidateRejectedStratagem,
        RejectedStratagem,
        AuthRejectedStratagem {

}
