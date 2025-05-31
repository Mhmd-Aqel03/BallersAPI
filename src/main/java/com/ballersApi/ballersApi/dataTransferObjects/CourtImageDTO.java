package com.ballersApi.ballersApi.dataTransferObjects;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CourtImageDTO {
    private final long id;
    private final String photoUrl;
    private final long courtId;

}
