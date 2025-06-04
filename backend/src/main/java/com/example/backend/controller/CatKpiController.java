package com.example.backend.controller;

import com.example.backend.entity.CatKpi;
import com.example.backend.service.CatKpiService;
import com.opencsv.CSVWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/kpis")
public class CatKpiController {

    private final CatKpiService service;

    public CatKpiController(CatKpiService service) {
        this.service = service;
    }

    @GetMapping
    public List<CatKpi> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatKpi> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CatKpi create(@RequestBody CatKpi catKpi) {
        return service.save(catKpi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatKpi> update(@PathVariable Integer id, @RequestBody CatKpi catKpi) {
        return service.findById(id)
                .map(existing -> {
                    catKpi.setId(id);
                    return ResponseEntity.ok(service.save(catKpi));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv() throws IOException {
        List<CatKpi> kpis = service.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            writer.writeNext(new String[]{"id","kpiName","kpiGroupId","description","status","insertTime","updateTime","userUpdate","tableName"});
            for (CatKpi kpi : kpis) {
                writer.writeNext(new String[]{
                        String.valueOf(kpi.getId()),
                        kpi.getKpiName(),
                        String.valueOf(kpi.getKpiGroupId()),
                        kpi.getDescription(),
                        kpi.getStatus() == null ? "" : kpi.getStatus().toString(),
                        kpi.getInsertTime() == null ? "" : kpi.getInsertTime().toString(),
                        kpi.getUpdateTime() == null ? "" : kpi.getUpdateTime().toString(),
                        kpi.getUserUpdate(),
                        kpi.getTableName()
                });
            }
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cat_kpi.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(out.toByteArray());
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) { // skip header
                    first = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    CatKpi kpi = new CatKpi();
                    if (!parts[0].isBlank()) {
                        kpi.setId(Integer.parseInt(parts[0]));
                    }
                    kpi.setKpiName(parts[1]);
                    kpi.setKpiGroupId(Integer.parseInt(parts[2]));
                    kpi.setDescription(parts[3]);
                    if (!parts[4].isBlank()) {
                        kpi.setStatus(Short.parseShort(parts[4]));
                    }
                    kpi.setUserUpdate(parts[7]);
                    kpi.setTableName(parts[8]);
                    service.save(kpi);
                }
            }
            return ResponseEntity.ok("Imported successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
