package ru.testqa.resource;

import ru.testqa.model.Films;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Path("/films")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,})
@Api(value = "Films Data Base")
public class FilmsDataBaseResource {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FilmsDataBaseResource(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @GET
    @ApiOperation(value = "Get List of all Films", notes = "Returns all films from the Data Base", response = Films.class, responseContainer = "List")
    public List<Films> ListFilms() {

        String sql = "select * from film";

        return namedParameterJdbcTemplate.query(sql, new FilmsMapper());
    }

    @GET
    @Path("/{filmId}")
    @ApiOperation(value = "Get a film by ID", notes = "Returns information about a film by ID", response = Films.class)
    public Films getFilm(
            @ApiParam(value = "The film ID", required = true) @PathParam("filmId") Integer filmId)
    {
        String sql = "select * from film where id=:filmId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("filmId", filmId);
        return namedParameterJdbcTemplate.query(sql, namedParameters, new FilmsMapper()).get(0);
    }

    @POST
    @ApiOperation(value = "Add a new film", notes = "Add a new film into the DB")
    public String createFilm(final Films film) {
        String sql = "insert into film values(:id, :name, :releaseDate, :reviewScore, :category, :rating)";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(film);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        return "{\"status\": \"Record Added Successfully\"}";
    }

    @PUT
    @Path("/{filmId}")
    @ApiOperation(value = "Update information about a film", notes = "Update an existing film in the DB ", response = Films.class)
    public Films editFilm(final Films film, @PathParam("filmId") Integer filmId ) {
        String sql = "update film set id=:id, name=:name, released_on=:releaseDate, review_score=:reviewScore, category=:category, rating=:rating where id=:id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(film);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);

        sql = "select * from film where id=:filmId";
        SqlParameterSource namedParameters2 = new MapSqlParameterSource("filmId", filmId);
        return namedParameterJdbcTemplate.query(sql, namedParameters2, new FilmsMapper()).get(0);
    }

    @DELETE
    @Path("/{filmId}")
    @ApiOperation(value = "Delete a film", notes = "Deletes a film from the Data Base by ID")
    public String deleteFilm(
            @ApiParam(value = "The film ID", required = true) @PathParam("filmId") Integer filmId)
    {
        String sql = "delete from film where id =:filmId";
        SqlParameterSource namedParameters = new MapSqlParameterSource("filmId", filmId);
        this.namedParameterJdbcTemplate.update(sql, namedParameters);
        return "{\"status\": \"A film deleted successfully\"}";
    }


    private static final class FilmsMapper implements RowMapper<Films> {
        public Films mapRow(ResultSet rs, int rowNum) throws SQLException {

            Films film = new Films();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setReleaseDate(rs.getDate("released_on"));
            film.setReviewScore(rs.getInt("review_score"));
            film.setCategory(rs.getString("category"));
            film.setRating(rs.getString("rating"));
            return film;
        }
    }
}
