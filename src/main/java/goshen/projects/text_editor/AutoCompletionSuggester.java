package goshen.projects.text_editor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Slf4j
@Component
public class AutoCompletionSuggester {
    Trie<String, String> trie = new PatriciaTrie<>();

    @PostConstruct
    public void init() {
        try {
            var objectMapper = new ObjectMapper();
            var json = objectMapper.readTree(new File("src/main/resources/data/autocompletion-dataset.json"));
            loadTrie(json);
        }
        catch (IOException e) {
            log.error("could not load file: {}", e.getMessage());
        }
    }

    public List<String> getSuggestions(String prefix) {
        SortedMap<String, String> prefixMap = trie.prefixMap(prefix);

        var suggestions = new ArrayList<String>();
        for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
            suggestions.add(entry.getKey());
        }
        return suggestions;
    }

    private void loadTrie(JsonNode json) {
        var fields = json.fields();
        while (fields.hasNext()) {
            var field = fields.next();
            trie.put(field.getKey(), String.valueOf(field.getValue()));
        }
    }
}
