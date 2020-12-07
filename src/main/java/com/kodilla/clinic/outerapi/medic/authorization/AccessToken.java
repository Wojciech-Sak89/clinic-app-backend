package com.kodilla.clinic.outerapi.medic.authorization;

import lombok.Getter;

@Getter
public class AccessToken {
    public String Token;
    public int ValidThrough;
}
