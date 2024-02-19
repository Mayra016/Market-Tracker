package com.Market.Tracker.Models;

import java.util.List;
import com.Market.Tracker.Entities.Actives;

public record UserDTO(String email, List<Actives> userActives) {
}
