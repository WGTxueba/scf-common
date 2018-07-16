package com.bytter.scf.core.json;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MybatisPlusPageJsonSerializer extends JsonSerializer<Page> {

    @Override
    public void serialize(Page page, JsonGenerator jsonGen, SerializerProvider serializerProvider) throws IOException {

        ObjectMapper om = new ObjectMapper()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        jsonGen.writeStartObject();
        jsonGen.writeFieldName("size");
        jsonGen.writeNumber(page.getSize());
        jsonGen.writeFieldName("pages");
        jsonGen.writeNumber(page.getPages());
        jsonGen.writeFieldName("total");
        jsonGen.writeNumber(page.getTotal());
        jsonGen.writeFieldName("current");
        jsonGen.writeNumber(page.getCurrent());
        jsonGen.writeFieldName("records");
        jsonGen.writeRawValue(om.writerWithView(serializerProvider.getActiveView())
                .writeValueAsString(page.getRecords()));
        jsonGen.writeEndObject();
    }

}
