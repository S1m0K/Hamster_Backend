package com.example.hamster_backend.api;

import com.example.hamster_backend.model.entities.TerrainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hamster_backend.service.TerrainObjectService;

@RestController
@RequestMapping("/terrainObject")
public class TerrainObjectController {
    @Autowired
    TerrainObjectService terrainObjectService;

    //TODO  getAllTerrainNames() --> and Ids
    //TODO  getTerrain(<TerrainId>)
    //TODO  deleteTerrain(<TerrainId>)
    //TODO  save/updateTerrain(<TerrainObject>) --> if Id null => createTerrain


}
