package com.mgreau.book.wildfly.booking.taskqueue;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

@SuppressWarnings("serial")
public class AddToQueueServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String filename = req.getParameter("filename");
		String size = req.getParameter("size");
		if (size == null)
			size = "";

		//Add a task in the push Queue
		Queue queue = QueueFactory.getDefaultQueue();
		TaskHandle t = queue.add(withUrl("/tasks/loadDatas").method(Method.GET).param(
				"fileKey", filename).param("size", size));

		req.setAttribute("msg", "Task" + t.getName() + "have be added to the queue");
		req.getRequestDispatcher("/jsp/taskqueue.jsp").forward(req, resp);
	}

}
