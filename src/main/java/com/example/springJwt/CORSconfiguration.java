package com.example.springJwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSconfiguration {
    @Bean
    public CorsFilter corsFilter() {
        //create new url configuration for browsers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //create new cors configuration....
        CorsConfiguration config = new CorsConfiguration();
        //allow to get credentials in cors
        config.setAllowCredentials(true);
        //allow to get from any ip/domain
        config.addAllowedOrigin("*");
        //allow to get any header
        config.addAllowedHeader("*");
        //alow to get methods
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        //allow to get any route -> localhost:8080/api/lecturer -> /api/lecture is route
        source.registerCorsConfiguration("/**", config);
        ///return new configuration
        return new CorsFilter(source);
    }
    @Configuration
    public class MyRestTemplate {
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return builder
                    //set connection time out for 3 sec
                    .setConnectTimeout(Duration.ofMillis(3000))
                    //set read time out for 3 sec.
                    .setReadTimeout(Duration.ofMillis(3000))
                    .build();
        }
    /*
    @Bean
    public RestTemplate restTemplateNoBuilder(){
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }
    */
    }
    @Component
    @Order(1)
    public class TesterRestTemplate implements CommandLineRunner {
        @Autowired
        RestTemplate restTemplate ;  //using restTemplate for getting rest from sites


        /**
         * we will get in this method, only the string instance from the rest
         * used to demonstrate the capabilities of restTemplate
         * @param args not important
         * @throws Exception not important
         */
        @Override
        public void run(String... args) throws Exception {
            //define our url location (string)
            String url = "http://localhost:8080/school/all";
            //call restTemplate for get on object of type string and move it to our variable
            String result = restTemplate.getForObject(url,String.class);
            //print our awosome job, get higher salary
            System.out.println(result);
        }
        @Override
        public void run(String... args) throws Exception {
    /*
    //define our url location (string)
    //String url = "http://localhost:8080/school/all";
    //call restTemplate for get on object of type string and move it to our variable
    //String result = restTemplate.getForObject(url,String.class);
    //print our awosome job, get higher salary
    //System.out.println(result);



    String allLecturerUrl = "http://localhost:8080/school/all";
    //ResponseEntity – for getting the response from the server with all the data that included , we need to use [] because json will be returned in [] and not as list 
    ResponseEntity<Lecturer[]> lecturerResult = restTemplate.getForEntity(allLecturerUrl,Lecturer[].class);
    //since we got an array from the response (we need to extract the body) we will convert it to List with Arrays.asList method 
    List<Lecturer> allLecturer = Arrays.asList(lecturerResult.getBody());
    System.out.println(allLecturer);
    System.out.println("===========================================================");
    System.out.println(allLecturer.get(1));

    //get one object :)
    String lecturerURL = "http://localhost:8080/school/lecturer/2";
    //we can get on object from the server and transfer it to instance
    Lecturer result = restTemplate.getForObject(lecturerURL, Lecturer.class);
    System.out.println(result);
    */

            getLecturer("2");


        }

        private void getLecturer(String id){

            String lecturerURL = "http://localhost:8080/school/lecturer/{id}";
            //we create a HashMap which will reflect our params
            Map<String,String> params = new HashMap<>();
            //we putting for id the number that we need
            params.put("id",id);
            Lecturer result = restTemplate.getForObject(lecturerURL, Lecturer.class,params);
            System.out.println(result);
        }
        String addUrl = "http://localhost:8080/school/lecturer/add";
        //create an object of lecturerrrrr
        Lecturer lecturer = Lecturer.builder()
                .name("Natan the king")
                .salary(150_000)
                .build();

         //post the object using REST with restTemplate :)
        restTemplate.postForEntity(addUrl,lecturer,Lecturer.class);

    }
}