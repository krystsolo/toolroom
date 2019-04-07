package com.manageyourtools.toolroom.domains;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {

    private String token;
    private String username;


}
