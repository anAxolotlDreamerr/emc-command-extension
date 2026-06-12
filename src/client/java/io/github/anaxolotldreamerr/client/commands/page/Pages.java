package io.github.anaxolotldreamerr.client.commands.page;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.*;

import java.util.*;

public class Pages {
    private static final ChatComponent CC = Minecraft.getInstance().gui.getChat();
    private final List<Page> PAGES = new ArrayList<>();
    public Pages(Page fisrtPage){
        add(fisrtPage);
    }
    public Pages(List<Page> pages){
        for(Page page : pages){
            add(page);
        }
    }
    public List<Page> pages(){
        return List.copyOf(PAGES);
    }
    public Pages add(Page page){
        PAGES.add(page);
        return this;
    }
    public boolean exist(int pages){
        return pages <= PAGES.size();
    }
    public void show(int pages){
        if(!exist(pages)){
            throw new NullPointerException("the pages is out of the range:("+1+"<=pages<="+PAGES.size()+" your pages is "+pages+")");
        }
        Page page = PAGES.get(pages-1);
        MutableComponent component = Component.empty();
        component.append(Component.literal("=".repeat(35)+"\n").withStyle(ChatFormatting.YELLOW));
        if(page.existTitle()){
            component.append(page.TITLE);
            component.append(Component.literal("-".repeat(35)+"\n").withStyle(ChatFormatting.WHITE));
        }
        for(Component c : page.LINES){
            component.append(c);
        }
        if(page.existTitle()){
            component.append(Component.literal("-".repeat(35)+"\n").withStyle(ChatFormatting.WHITE));
        }
        component.append(Component.literal(" ".repeat(16)+pages+"/"+PAGES.size()+"\n"));
        component.append(Component.literal("=".repeat(35)+"\n").withStyle(ChatFormatting.YELLOW));
        CC.addMessage(component);
    }
    public static class Page {
        private final Component TITLE;
        private final List<Component> LINES = new ArrayList<>();
        public Page(Component title){
            this.TITLE = title;
        }
        public static Page setTitle(Component title){
            return new Page(title);
        }
        public static Page untitled(){
            return new Page(null);
        }
        public Page addLine(Component line){
            LINES.add(line);
            return this;
        }
        public Component getLine(int row){
            return LINES.get(row);
        }
        public List<Component> lines(){
            return List.copyOf(LINES);
        }
        public Component title(){
            return TITLE;
        }
        public static Page copyOf(Page page){
            Page replica = new Page(page.TITLE);
            for(Component line : page.LINES)
                replica.addLine(line);
            return replica;
        }
        public  boolean existTitle(){
            return TITLE != null&&!TITLE.getString().isEmpty();
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Page page)) return false;
            return Objects.equals(TITLE, page.TITLE) && Objects.equals(LINES, page.LINES);
        }

        @Override
        public int hashCode() {
            return Objects.hash(TITLE, LINES);
        }

        @Override
        public String toString() {
            return "Page{" +
                    "TITLE='" + TITLE + '\'' +
                    ", LINES=" + LINES +
                    '}';
        }
    }
    public static class LimitedPage extends Page{
        private int rows = 1;
        public LimitedPage(Component title) {
            super(title);
        }
        public LimitedPage(Component title, int rows) {
            super(title);
            if(rows < 1){
                throw new IllegalArgumentException("rows can't lower than 1 (rows:"+rows+")");
            }
            this.rows = rows;
        }
        public LimitedPage setRows(int rows){
            if(rows < 1){
                throw new IllegalArgumentException("rows can't lower than 1 (rows:"+rows+")");
            }
            this.rows = rows;
            return this;
        }
        public int getRows(){
            return rows;
        }
        public Pages generate(){
            List<Page> pages = new ArrayList<>();
            Page page = null;
            int count = 0;
            for(Component line:lines()){
                if(count % rows==0){
                    if(page != null) {
                        pages.add(page);
                    }
                    page = new Page(title());
                }
                page.addLine(line);
                count++;
            }
            return new Pages(pages);
        }
    }
}
