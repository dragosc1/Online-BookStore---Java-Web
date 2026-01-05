package com.bookstore.controller.admin;

import com.bookstore.dto.request.AuthorRequestDto;
import com.bookstore.dto.response.AuthorResponseDto;
import com.bookstore.model.Author;
import com.bookstore.service.AuthorService;
import com.bookstore.util.AuthorMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/authors")
@PreAuthorize("hasRole('ADMIN')")
@Tag(
        name = "Admin Author Management",
        description = "Admin-only endpoints for managing authors"
)
@SecurityRequirement(name = "bearerAuth")
public class AdminAuthorController {

    private final AuthorService authorService;

    public AdminAuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // ========= CREATE =========
    @Operation(
            summary = "Create author",
            description = "Creates a new author. Admin access required."
    )
    @PostMapping("/create")
    public ResponseEntity<AuthorResponseDto> createAuthor(
            @RequestBody AuthorRequestDto dto) {

        Author author = AuthorMapper.toEntity(dto);
        Author saved = authorService.saveAuthor(author);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthorMapper.toResponseDto(saved));
    }

    // ========= READ =========
    @Operation(
            summary = "Get author by ID",
            description = "Fetch an author using its unique ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(
            @PathVariable Long id) {

        Optional<Author> authorOpt = authorService.getAuthorById(id);

        return authorOpt
                .map(author -> ResponseEntity.ok(AuthorMapper.toResponseDto(author)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get all authors",
            description = "Retrieve a list of all authors"
    )
    @GetMapping
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {

        List<AuthorResponseDto> authors = authorService.getAllAuthors()
                .stream()
                .map(AuthorMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(authors);
    }

    // ========= UPDATE =========
    @Operation(
            summary = "Update author",
            description = "Update an existing author by ID"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorRequestDto dto) {

        Optional<Author> authorOpt = authorService.getAuthorById(id);
        if (authorOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Author author = authorOpt.get();
        AuthorMapper.updateEntity(author, dto);
        Author updated = authorService.saveAuthor(author);

        return ResponseEntity.ok(AuthorMapper.toResponseDto(updated));
    }

    // ========= DELETE =========
    @Operation(
            summary = "Delete author",
            description = "Delete an author by ID"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id) {

        if (authorService.getAuthorById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }
}
