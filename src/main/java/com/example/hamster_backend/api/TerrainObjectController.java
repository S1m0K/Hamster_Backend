package com.example.hamster_backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hamster_backend.service.TerrainObjectService;

@RestController
@RequestMapping("/terrainObject")
public class TerrainObjectController {
    @Autowired
    TerrainObjectService terrainObjectService;

}
