package main.java.com.pradas.jopma;

import main.java.com.pradas.jopma.utils.Request;

import java.net.MalformedURLException;

public class MainTestRequest {
    public static void main(String[] args) {

        String url = "http://ec2-52-56-227-247.eu-west-2.compute.amazonaws.com/api/users";
        String type = "GET";
        String headers = "Authorization=Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQyYmM5MGJiMjI0MjQ5MzA5YmI4OTM5NTk3YWExMWFjMzhmZDc0NjM3ZmYzOTU4Mzk3YjgzOTA5YzFlMTcxMzI3YmJjOWQxYTBmOTIzYzQwIn0.eyJhdWQiOiIyIiwianRpIjoiNDJiYzkwYmIyMjQyNDkzMDliYjg5Mzk1OTdhYTExYWMzOGZkNzQ2MzdmZjM5NTgzOTdiODM5MDljMWUxNzEzMjdiYmM5ZDFhMGY5MjNjNDAiLCJpYXQiOjE1MTQ4MjgyMTcsIm5iZiI6MTUxNDgyODIxNywiZXhwIjoxNTE0ODMxODE3LCJzdWIiOiIiLCJzY29wZXMiOltdfQ.G0Tt92FfEZyxxp6ZdGIYDhnVJKkTT5cKn1AK8BjWRG35TG2pQydU9oPluaQ289b6QD-ZTccaZ5HkxONaQWkzt9Irz1L4KGBqMgfNX01vsnSZzwAxC0vUS7UFqit7xXwkEeE1GxkXs0h9xj9raBP8gIC_60s6evdzTRiskasoPK_IXfMgPEV3dhOTmVvg5EyeWvDh-pVNAS12RevcDLB53p0t-YQ3ZGIKAUi7jVV8k2r7kvwwcSaYqNQ-8ap8-c5Huro_lpblbWmdNa3kPIGHi8XDV5NICaJ39i7a1gPkttmaR5lvbyqG95NePy39rpOe3YNQx6ePEjFQVDRYjjG3iMun65dhH16whG90waYQCoCgkPC7PX9YSdJUTIDzCeXgLSP7rt3UMauqz7CkjZ7nxkkFC9EaZ_QrsIeuYzZumQr2sfz0dvhZaPTNdfwwoQi75manQFgQxYLXZZ1hOcp2VGS9_X0xy-8cJrqJ6CA2MkphJe1Fko2U8OZ5U6bIzx1kU7s1Gn--6rI_3bkKbdlTi90PbNBqo0LCannD2Q40frOtSt1vSU_G4nPbn90GObDeP9aILIbdr7qySpm1S5rxtgFaEfED1O6jMXJOqxo14C5E8nMI5QyDU9ay2CwPpteIifWqHiQ8Amg2voFlLr-ZxRANiTLe7yP6wgZbe_VayvE";
        String parameters = "";

        try {
            Request req = new Request(url, type, headers, parameters);
            String response = req.doRequest();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
