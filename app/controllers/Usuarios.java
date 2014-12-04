package controllers;

import helpers.ControllerHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Usuario;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * 
 * @authors Luis Sedano de la Rosa
 * 		    José Manuel Paredes Garcia
 *
 */
public class Usuarios extends Controller {
	
	/**
	 * Action method GET /usuarios/<pag>.
	 * This method return a JSON with all users
	 * @param Page
	 */
	public static Result index(Integer pag) {
		Result res;
		
		String paginaSize = request().getQueryString("size");
		if (paginaSize == null) {
			paginaSize = "10";
		}
		
		if (ControllerHelper.acceptsJson(request())) {
			Map<String, Object> result = new HashMap<String, Object>();
			
			List<Usuario> usuarios = Usuario.findAll(pag, Integer.valueOf(paginaSize));
			Integer count = Usuario.finder.findRowCount();
			result.put("Numero de usuarios", count);
			result.put("usuarios", usuarios);
			
			res = ok(Json.toJson(result));
		}
		/*else if (ControllerHelper.acceptsXml(request())) {
			res = ok(views.xml.usuarios.render(lista, count));
		}*/
		else {
			res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
		}
			
		return res; 
	}
	
	/**
	 * Action method POST /usuario/<uId>.
	 * This method adds a user and return a JSON with user's data created.
	 * The user attributes must be inserted into the body
	 */
	public static Result create() {
		Form<Usuario> form = Form.form(Usuario.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(2, "invalid_user", form.errorsAsJson()));
		}

		Usuario usuario = form.get();
		
		usuario.save();
		
		// Esto implementa una característica de hypermedia: devolvemos la URL para consultar
		// los detalles del usuario
		response().setHeader(LOCATION, routes.Usuarios.retrieve(usuario.getId()).absoluteURL(request()));

		return created(Json.toJson(usuario));
	}
	
	/**
	 * Action method para GET /usuario/<uId>
	 * This method return a JSON with user's data
	 * 
	 * @param uId identificador del usuario
	 */
	public static Result retrieve(Long uId) {
		Result res;
		
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			res = notFound();
		}
		else {
			if (ControllerHelper.acceptsJson(request())) {
				res = ok(Json.toJson(usuario));
			}
			/*else if (ControllerHelper.acceptsXml(request())) {
				res = ok(views.xml._usuario.render(usuario));
			}*/
			else {
				res = badRequest(ControllerHelper.errorJson(1, "unsupported_format", null));
			}
		}
		
		return res;
	}

	/**
	 * Action method PUT /usuario/<uId>
	 * This method updates the data of a user and return "ok result" message 
	 * if the upgraded was correctly.
	 * New user attributes must be inserted into the body.
	 * 
	 * @param Id User
	 */
	public static Result update(Long uId) {
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			return notFound();
		}
		
		Form<Usuario> form = Form.form(Usuario.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(ControllerHelper.errorJson(1, "invalid_user", form.errorsAsJson()));
		}

		Result res;

		if (usuario.changeData(form.get())) {
			usuario.save();
			res = ok();
		}
		else {
			res = status(NOT_MODIFIED);
		}
		
		return res;
	}
	
	/**
	 * Action method DELETE /usuario/<uId>
	 * This method delete a user and return "ok result" message if
	 * the user was deleted correctly.
	 * 
	 * @param Id User
	 */
	public static Result delete(Long uId) {
		Usuario usuario = Usuario.finder.byId(uId);
		if (usuario == null) {
			return notFound();
		}

		usuario.delete();

		return ok();
	}
}
