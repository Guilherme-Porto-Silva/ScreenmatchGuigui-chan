package silva.porto.guilherme.Screenmatch.service.translate;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class AskChatGPT {

    private final OpenAiService service;

    public AskChatGPT(String key) { service = new OpenAiService(key); }



    private String responder(String userRequest) {

        CompletionRequest request = CompletionRequest.builder()

                .model("gpt-3.5-turbo-instruct")

                .prompt(userRequest)

                .maxTokens(2000)

                .temperature(0.7)

                .build();

        var response = service.createCompletion(request);

        return response.getChoices().getFirst().getText();
    }



    private String translatePromptBuilder (String text) {

        return "Traduza o texto \"" + text + "\" para o português do Brasil.";
    }

    private String translatePromptBuilder (String text, String language) {

        return "Translate the text \"" + text + "\" to " + language + '.';
    }

    private String translatePromptBuilder (String text, String language, String country) {

        return "Translate the text \"" + text + "\" to the " + language + " of " + country + '.';
    }



    private String chosePromptBuilderTranslate(String... args) {

        if (args.length == 1) return translatePromptBuilder(args[0]);

        if (args.length == 2) return translatePromptBuilder(args[0], args[1]);

        if (args.length == 3) return translatePromptBuilder(args[0], args[1], args[2]);

        throw new IllegalArgumentException("Not preparade for that amount of arguments.");
    }
    
    

    public String translate (String... args) {
        
        return responder(chosePromptBuilderTranslate(args));
    }
}