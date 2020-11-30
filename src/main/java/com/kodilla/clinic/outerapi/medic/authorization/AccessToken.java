package com.kodilla.clinic.outerapi.medic.authorization;

import lombok.Getter;

@Getter
public class AccessToken {

    /// <summary>
    /// Token string
    /// </summary>
    public String Token;

    /// <summary>
    /// Valid period of token in seconds
    /// </summary>
    public int ValidThrough;
}
