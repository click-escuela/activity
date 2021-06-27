package click.escuela.activity.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.exception.ActivityException;
import click.escuela.activity.service.impl.ActivityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
	

	@Operation(summary = "Get activity by schoolId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityDTO.class))) })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ActivityDTO>> getBySchool(
			@Parameter(name = "School Id", required = true) @PathVariable("schoolId") String schoolId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(activityService.getBySchool(schoolId));
	}
	
	@Operation(summary = "Get activity by courseId", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityDTO.class))) })
	@GetMapping(value = "course/{courseId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ActivityDTO>> getByCourse(
			@Parameter(name = "Course Id", required = true) @PathVariable("courseId") String courseId) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(activityService.getByCourse(courseId));
	}


	@Operation(summary = "Create Activity", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ActivityMessage> create(@RequestBody @Validated ActivityApi activityApi)
			throws ActivityException {
		activityService.create(activityApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ActivityMessage.CREATE_OK);
	}

	@Operation(summary = "Update Activity", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@PutMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ActivityMessage> update(@RequestBody @Validated ActivityApi activityApi)
			throws ActivityException {
		activityService.update(activityApi);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ActivityMessage.UPDATE_OK);
	}

	@Operation(summary = "Delete Activity", responses = {
			@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")) })
	@DeleteMapping(value = "/{activityId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ActivityMessage> delete(
			@Parameter(name = "Activity id", required = true) @PathVariable("activityId") String activityId)
			throws ActivityException {
		activityService.delete(activityId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ActivityMessage.DELETE_OK);
	}

}
