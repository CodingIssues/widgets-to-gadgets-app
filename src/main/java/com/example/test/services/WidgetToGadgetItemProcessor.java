package com.example.test.services;


import org.springframework.batch.item.ItemProcessor;


import com.example.test.models.Widget;
import com.example.test.models.Gadget;


public class WidgetToGadgetItemProcessor  implements ItemProcessor<Widget, Gadget> {

    @Override
    public Gadget process(final Widget widget) throws Exception {
        Gadget gadget = new Gadget();
        gadget.setGadget_id(widget.id);

        gadget.setCreationDate(widget.creationDate.toLocalDateTime());
        if( widget.lastUpdatedDate != null)
            gadget.setLastModifiedDate(widget.lastUpdatedDate.toLocalDateTime());
        
        return gadget;
    }



}