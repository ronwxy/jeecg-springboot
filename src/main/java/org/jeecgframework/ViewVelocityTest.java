package org.jeecgframework;


public class ViewVelocityTest {

    public static void main(String[] args) throws Exception {
//        Template template = RuntimeSingleton.getTemplate("classpath:/content/cms/cmsSite-list.vm", "UTF-8");
//        System.out.println(template);
//        File file = new File("D:\\github\\jeecg-springboot\\src\\main\\resources\\content\\cms\\cmsSite-list.vm");
//        if(file.canRead()){
//            System.out.println("file exist");
//        }else{
//            System.out.println("file not exist");
//        }

//        InputStream ins = ClassUtils.getResourceAsStream(new ViewVelocityTest().getClass(), "content/cms/cmsSite-list.vm");
//        BufferedReader br = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
//        System.out.println(br.lines().toArray()[0]);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class.forName("com.jeecg.core.biz.tag.select.dict.tag.SelectDictTag", true, loader);

    }
}
