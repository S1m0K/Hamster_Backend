package com.example.hamster_backend.api;

import com.example.hamster_backend.model.entities.TerrainObject;
import com.example.hamster_backend.model.entities.User;
import com.example.hamster_backend.service.TerrainObjectService;
import com.example.hamster_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/terrainObject")
public class TerrainObjectController {
    @Autowired
    TerrainObjectService terrainObjectService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "save")
    public void saveProgram(@RequestBody TerrainObject terrainObject) {
        terrainObjectService.save(terrainObject);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updatePath")
    public void updatePath(@RequestBody TerrainObject terrainObject) {
        terrainObjectService.updatePath(terrainObject);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updateName")
    public void updateName(@RequestBody TerrainObject terrainObject) {
        terrainObjectService.updateName(terrainObject);
    }


    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = "/delete/{terrainObject_id}")
    public void deleteNotification(@PathVariable("terrainObject_id") long terrainObjectId) {
        terrainObjectService.delete(terrainObjectId);
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "get/{terrainObject_id}")
    public TerrainObject getProgram(@PathVariable("terrainObject_id") long terrainObject_id) {
        return terrainObjectService.getTerrainObject(terrainObject_id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "getBasicData")
    public Set<TerrainObject> getTerrainBasicData(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return terrainObjectService.getTerrainObjectBasicData(user.getId());
    }


}
