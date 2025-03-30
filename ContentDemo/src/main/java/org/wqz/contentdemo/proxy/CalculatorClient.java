package org.wqz.contentdemo.proxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class CalculatorClient {
    private static final String SERVER_URL = "http://localhost:8080/rpc";

    public static int add(int a, int b) throws IOException {
        // 创建请求对象
        URL url = new URL(SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // 封装参数为 JSON 格式
        RpcRequest request = new RpcRequest("CalculatorService", "add", new Object[]{a, b});
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);

        // 发送请求
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(jsonRequest);
        }

        // 读取响应
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            RpcResponse rpcResponse = gson.fromJson(response.toString(), RpcResponse.class);
            return (int) rpcResponse.getResult();
        }
    }

    // 简单的 RPC 请求类
    static class RpcRequest {
        private String serviceName;
        private String methodName;
        private Object[] parameters;

        public RpcRequest(String serviceName, String methodName, Object[] parameters) {
            this.serviceName = serviceName;
            this.methodName = methodName;
            this.parameters = parameters;
        }

        // getters and setters
        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Object[] getParameters() {
            return parameters;
        }

        public void setParameters(Object[] parameters) {
            this.parameters = parameters;
        }
    }

    // 简单的 RPC 响应类
    static class RpcResponse {
        private Object result;

        public RpcResponse(Object result) {
            this.result = result;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
        }
    }
}