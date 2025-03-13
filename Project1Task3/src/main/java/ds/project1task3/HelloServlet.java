package ds.project1task3;

import java.io.*;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet",
        urlPatterns = {"/search-park"})
public class HelloServlet extends HttpServlet {
    private String message;
    private ParksHandler parksHandler;
    public void init() {
        parksHandler = new ParksHandler();

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String path = request.getServletPath();
        if (path.equals("/search-park")){
            getParkInfo(request, response);
        } else{
//            String nextView = "index.jsp";
//            RequestDispatcher view = request.getRequestDispatcher(nextView);
//            view.forward(request, response);
        }

    }

    private void getParkInfo(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String park_code = request.getParameter("parks_code");

//        Get necessary park info
        String parkName = parksHandler.getName(park_code);
        request.setAttribute("parkName", parkName);

        String[] weather = parksHandler.getWeatherInfo(park_code);
        request.setAttribute("weather", weather);
//        System.out.println(weather);

        String imageURL = parksHandler.getParkImage(park_code);
        request.setAttribute("imageURL", imageURL);

        List<String> activites = parksHandler.getParkActivites(park_code);
        request.setAttribute("activites", activites);
//        System.out.println(activites);

//        Load Next view
        String nextView = "parkinfo.jsp";
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }

    public void destroy() {
    }
}