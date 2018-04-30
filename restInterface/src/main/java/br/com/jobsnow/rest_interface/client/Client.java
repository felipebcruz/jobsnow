package br.com.jobsnow.rest_interface.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
	private final HttpClient client;
	private HttpResponse response;
	private HttpGet get;
	private HttpPost post;
	private HttpPatch patch;
	private HttpHead head;
	private HttpDelete delete;
	private final ContentType contentType = ContentType.APPLICATION_JSON;
	
	public Client() {
		this.client = HttpClientBuilder.create().build();
	}

	public Client(HttpClient client, HttpResponse response, HttpGet get) {
		this.client = client;
		this.response = response;
		this.get = get;
	}
	
	public Client(HttpClient client, HttpResponse response, HttpHead head) {
		this.client = client;
		this.response = response;
		this.head = head;
	}

	public Client(HttpClient client, HttpResponse response, HttpPatch patch) {
		this.client = client;
		this.response = response;
		this.patch = patch;
	}

	public Client(HttpClient client, HttpResponse response, HttpDelete delete) {
		this.client = client;
		this.response = response;
		this.delete = delete;
	}

	public Client(HttpClient client, HttpResponse response, HttpPost post) {
		this.client = client;
		this.response = response;
		this.post = post;
	}

	public InputStream _doGet(String url){
		if(this.get == null) {
			this.get = new HttpGet(url);
		}
		
		try {
			this.response = this.client.execute(this.get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
		
		StatusLine statusLine = this.response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		boolean isStatusNotOk = statusCode != 200;
		
		if (isStatusNotOk) {
			this._getErrorMessage();
		}
		 
		HttpEntity entity = this.response.getEntity();
		InputStream content;
		try {
			content = entity.getContent();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
		
		return content;
	}
	
	public boolean _doHead(String url) {
		if(this.head == null) {
			this.head = new HttpHead(url);
		}
		
		this.head.setHeader("Accept", "application/json");
		this.head.setHeader("Content-type", "application/json");
		
		try {
			this.response = this.client.execute(this.head);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
		
		StatusLine statusLine = this.response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		boolean isStatusOk = statusCode == 200;
		boolean isStatusNotFound = statusCode == 404;
		
		if(isStatusOk) {
			return true;
		}
		
		if(isStatusNotFound) {
			return false;
		}
		
		// TODO		
		throw new RuntimeException("");
	}

	public void _doPatch(String url, Map<String, String> corpoDeRequisicao) {
		if (this.patch == null) {
			this.patch = new HttpPatch(url);
		}
		
		StringEntity entity;
		try {
			entity = new StringEntity(new ObjectMapper().writeValueAsString(corpoDeRequisicao), this.contentType);
		} catch (UnsupportedCharsetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		this.patch.setHeader("Accept", "application/json");
		this.patch.setHeader("Content-type", "application/json");
		this.patch.setEntity(entity);

		try {
			this.response = this.client.execute(this.patch);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		StatusLine statusLine = this.response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		boolean isStatusNotNoContent = statusCode != 204;
		
		if (isStatusNotNoContent) {
			this._getErrorMessage();
		}
	}

	public void _doDelete(String url){
		if(this.delete == null) {
			this.delete = new HttpDelete(url);
		}
		
		this.delete.setHeader("Accept", "application/json");
		this.delete.setHeader("Content-type", "application/json");

		try {
			this.response = this.client.execute(this.delete);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		StatusLine statusLine = this.response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		boolean isStatusNotNoContent = statusCode != 204;
		
		if (isStatusNotNoContent) {
			this._getErrorMessage();
		}
	}

	public InputStream _doPost(String url, Map<String, String> corpoDeRequisicao){
		if (this.post == null) {
			this.post = new HttpPost(url);
		}
		
		StringEntity entity;
		try {
			entity = new StringEntity(new ObjectMapper().writeValueAsString(corpoDeRequisicao), this.contentType);
		} catch (UnsupportedCharsetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		this.post.setHeader("Accept", "application/json");
		this.post.setHeader("Content-type", "application/json");
		this.post.setEntity(entity);
		
		try {
			this.response = this.client.execute(this.post);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("",e);
		}
		
		StatusLine statusLine = this.response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		boolean isStatusNotCreated = statusCode != 201;
		
		if(isStatusNotCreated) {
			this._getErrorMessage();
		}
		
		HttpEntity responseEntity = this.response.getEntity();
		InputStream content;
		try {
			content = responseEntity.getContent();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
		
		return content;
	}

	private void _getErrorMessage() {
		HttpEntity entity = this.response.getEntity();
		InputStream content;
		try {
			content = entity.getContent();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
		
		Map<String, String> retorno = null;
		String line = "";
		
		try(
			InputStreamReader inputStreamReader = new InputStreamReader(content);
			BufferedReader rd = new BufferedReader(inputStreamReader);		
		) {
			while ((line = rd.readLine()) != null) {
				retorno = new ObjectMapper().readValue(line, new TypeReference<Map<String, String>>(){});
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("", e);
		}
		
		throw new RuntimeException("Falha ao fazer a requisi��o, HTTP error code: " + this.response.getStatusLine().getStatusCode() + ", motivo do erro: "+retorno.get("message"));
	}
}