package com.example;

import java.io.File;

// chcp 65001; put this i
public class Main2 {
    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        Agent myAgent = new Agent(new File("API_KEY.env"));
        myAgent.chat();
    }    
    
       /*curl http://localhost:1234/api/v1/chat \
  -H "Authorization: Bearer $LM_API_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "ibm/granite-4-micro",
    "input": "Tell me the top trending model on hugging face and navigate to https://lmstudio.ai",
    "integrations": [
      {
        "type": "ephemeral_mcp",
        "server_label": "huggingface",
        "server_url": "https://huggingface.co/mcp",
        "allowed_tools": [
          "model_search"
        ]
      },
      {
        "type": "plugin",
        "id": "mcp/playwright",
        "allowed_tools": [
          "browser_navigate"
        ]
      }
    ],
    "context_length": 8000,
    "temperature": 0
  }'
 */
}
