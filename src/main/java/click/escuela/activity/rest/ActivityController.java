package click.escuela.activity.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.exception.TransactionException;
import click.escuela.activity.service.impl.ActivityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/school/{schoolId}/activity")
public class ActivityController {

	@Autowired
	private ActivityServiceImpl activityService;

	// Metodo de prueba
	@Operation(summary = "Get all the bills", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityDTO.class))) })
	@GetMapping(value = "/getAll", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ActivityDTO>> getAllCourses() {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(activityService.findAll());
	}

	@Operation(summary = "Create Activity", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ActivityMessage> create(
			@RequestBody @Validated ActivityApi activityApi) throws TransactionException {
		activityService.create(activityApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ActivityMessage.CREATE_OK);
	}

}