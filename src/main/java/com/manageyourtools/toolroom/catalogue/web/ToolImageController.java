package com.manageyourtools.toolroom.catalogue.web;

import com.manageyourtools.toolroom.catalogue.domain.CatalogueFacade;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(ToolController.BASE_URL)
@AllArgsConstructor
public class ToolImageController {

    private final CatalogueFacade catalogueFacade;

    @PostMapping("/{toolId}/image")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(@RequestParam("file") MultipartFile file, @PathVariable Long toolId){
        this.catalogueFacade.uploadImage(file, toolId);
    }

    @GetMapping("/{toolId}/image")
    @ResponseStatus(HttpStatus.OK)
    public void getImage(@PathVariable Long toolId, HttpServletResponse response) throws IOException {

        response.setContentType("image/jpeg");
        InputStream in = new ByteArrayInputStream(this.catalogueFacade.findImage(toolId));
        IOUtils.copy(in, response.getOutputStream());
    }

}
