package org.x.tongnews.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayaseruri on 15/7/30.
 */
public class RawPosts {

    /**
     * pages : 5
     * count : 12
     * category : {"parent":0,"description":"","id":2,"post_count":57,"title":"Post","slug":"post"}
     * status : ok
     */
    private int pages;
    private int count;
    private CategoryEntity category;
    private ArrayList<PostsEntity> posts;
    private String status;
    public RawPosts(){

    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public void setPosts(ArrayList<PostsEntity> posts) {
        this.posts = posts;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPages() {
        return pages;
    }

    public int getCount() {
        return count;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public ArrayList<PostsEntity> getPosts() {
        return posts;
    }

    public String getStatus() {
        return status;
    }

    public static class CategoryEntity {
        /**
         * parent : 0
         * description :
         * id : 2
         * post_count : 57
         * title : Post
         * slug : post
         */
        private int parent;
        private String description;
        private int id;
        private int post_count;
        private String title;
        private String slug;

        public void setParent(int parent) {
            this.parent = parent;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setPost_count(int post_count) {
            this.post_count = post_count;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public int getParent() {
            return parent;
        }

        public String getDescription() {
            return description;
        }

        public int getId() {
            return id;
        }

        public int getPost_count() {
            return post_count;
        }

        public String getTitle() {
            return title;
        }

        public String getSlug() {
            return slug;
        }
    }

    public static class PostsEntity {
        /**
         * date : 2015-07-29 12:02:11
         * comment_count : null
         * thumbnail : null
         * comments : null
         * attachments : null
         * author : {"name":"海導[高级编辑]","nickname":"海導","last_name":"","description":"","id":2,"first_name":"海導[高级编辑]","slug":"qwe7009","url":""}
         * custom_fields : {"editor":["a:1:{i:0;s:6:\"海導\";}"],"thumbnail":["1706"],"video_link":["<embed width=\"569px\" height=\"440px\" type=\"application/x-shockwave-flash\" src=\"http://static.hdslb.com/miniloader.swf?aid=2586417&page=1\" />"],"like":["0"],"intro":["软软冰带你去有1200年历史的大矿坑，以为自己进入了AVG游戏探险。穿透2次元和3次元的结界，让软游记带给你与众不同的日本旅游体验！"],"float_comment":[""],"writter":["null"],"cameraman":["null"],"checklist":["1"],"viewer_count":["23"],"thumbnail_url":["http://54.92.122.102/wordpress/wp-content/uploads/2015/07/20150729111136-195x150.png"]}
         * type : post
         * title : 【圣地巡礼】软软冰带你去1200年历史的矿坑去填坑
         * comment_status : null
         * url : http://bk.tongnews.org/wordpress/2015/07/29/%e3%80%90%e5%9c%a3%e5%9c%b0%e5%b7%a1%e7%a4%bc%e3%80%91%e8%bd%af%e8%bd%af%e5%86%b0%e5%b8%a6%e4%bd%a0%e5%8e%bb1200%e5%b9%b4%e5%8e%86%e5%8f%b2%e7%9a%84%e7%9f%bf%e5%9d%91%e5%8e%bb%e5%a1%ab%e5%9d%91/
         * content : null
         * tags : [{"description":"","id":232,"post_count":1,"title":"兵库","slug":"%e5%85%b5%e5%ba%93","group":"not assigned"},{"description":"","id":10,"post_count":15,"title":"圣地巡礼","slug":"%e5%9c%a3%e5%9c%b0%e5%b7%a1%e7%a4%bc","group":"not assigned"},{"description":"","id":233,"post_count":1,"title":"生野银山","slug":"%e7%94%9f%e9%87%8e%e9%93%b6%e5%b1%b1","group":"not assigned"},{"description":"","id":8,"post_count":40,"title":"痛新闻","slug":"%e7%97%9b%e6%96%b0%e9%97%bb","group":"not assigned"}]
         * title_plain : 【圣地巡礼】软软冰带你去1200年历史的矿坑去填坑
         * modified : null
         * id : 1699
         * categories : [{"parent":0,"description":"","id":2,"post_count":57,"title":"Post","slug":"post"},{"parent":0,"description":"","id":7,"post_count":37,"title":"Video","slug":"video"}]
         * excerpt : null
         * slug : %e3%80%90%e5%9c%a3%e5%9c%b0%e5%b7%a1%e7%a4%bc%e3%80%91%e8%bd%af%e8%bd%af%e5%86%b0%e5%b8%a6%e4%bd%a0%e5%8e%bb1200%e5%b9%b4%e5%8e%86%e5%8f%b2%e7%9a%84%e7%9f%bf%e5%9d%91%e5%8e%bb%e5%a1%ab%e5%9d%91
         * status : publish
         */
        private String date;
        private String comment_count;
        private String thumbnail;
        private String comments;
        private String attachments;
        private AuthorEntity author;
        private CustomFieldsEntity custom_fields;
        private String type;
        private String title;
        private String comment_status;
        private String url;
        private String content;
        private ArrayList<TagsEntity> tags;
        private String title_plain;
        private String modified;
        private int id;
        private List<CategoriesEntity> categories;
        private String excerpt;
        private String slug;
        private String status;

        public PostsEntity(){

        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public void setAttachments(String attachments) {
            this.attachments = attachments;
        }

        public void setAuthor(AuthorEntity author) {
            this.author = author;
        }

        public void setCustom_fields(CustomFieldsEntity custom_fields) {
            this.custom_fields = custom_fields;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setComment_status(String comment_status) {
            this.comment_status = comment_status;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setTags(ArrayList<TagsEntity> tags) {
            this.tags = tags;
        }

        public void setTitle_plain(String title_plain) {
            this.title_plain = title_plain;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setCategories(List<CategoriesEntity> categories) {
            this.categories = categories;
        }

        public void setExcerpt(String excerpt) {
            this.excerpt = excerpt;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public String getComment_count() {
            return comment_count;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getComments() {
            return comments;
        }

        public String getAttachments() {
            return attachments;
        }

        public AuthorEntity getAuthor() {
            return author;
        }

        public CustomFieldsEntity getCustom_fields() {
            return custom_fields;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getComment_status() {
            return comment_status;
        }

        public String getUrl() {
            return url;
        }

        public String getContent() {
            return content;
        }

        public ArrayList<TagsEntity> getTags() {
            return tags;
        }

        public String getTitle_plain() {
            return title_plain;
        }

        public String getModified() {
            return modified;
        }

        public int getId() {
            return id;
        }

        public List<CategoriesEntity> getCategories() {
            return categories;
        }

        public String getExcerpt() {
            return excerpt;
        }

        public String getSlug() {
            return slug;
        }

        public String getStatus() {
            return status;
        }

        public static class AuthorEntity {
            /**
             * name : 海導[高级编辑]
             * nickname : 海導
             * last_name :
             * description :
             * id : 2
             * first_name : 海導[高级编辑]
             * slug : qwe7009
             * url :
             */
            private String name;
            private String nickname;
            private String last_name;
            private String description;
            private int id;
            private String first_name;
            private String slug;
            private String url;

            public AuthorEntity(){

            }

            public void setName(String name) {
                this.name = name;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getName() {
                return name;
            }

            public String getNickname() {
                return nickname;
            }

            public String getLast_name() {
                return last_name;
            }

            public String getDescription() {
                return description;
            }

            public int getId() {
                return id;
            }

            public String getFirst_name() {
                return first_name;
            }

            public String getSlug() {
                return slug;
            }

            public String getUrl() {
                return url;
            }
        }

        public static class CustomFieldsEntity {
            /**
             * editor : ["a:1:{i:0;s:6:\"海導\";}"]
             * thumbnail : ["1706"]
             * video_link : ["<embed width=\"569px\" height=\"440px\" type=\"application/x-shockwave-flash\" src=\"http://static.hdslb.com/miniloader.swf?aid=2586417&page=1\" />"]
             * like : ["0"]
             * intro : ["软软冰带你去有1200年历史的大矿坑，以为自己进入了AVG游戏探险。穿透2次元和3次元的结界，让软游记带给你与众不同的日本旅游体验！"]
             * float_comment : [""]
             * writter : ["null"]
             * cameraman : ["null"]
             * checklist : ["1"]
             * viewer_count : ["23"]
             * thumbnail_url : ["http://54.92.122.102/wordpress/wp-content/uploads/2015/07/20150729111136-195x150.png"]
             */
            private List<String> editor;
            private List<String> thumbnail;
            private List<String> video_link;
            private List<String> like;
            private List<String> intro;
            private List<String> float_comment;
            private List<String> writter;
            private List<String> cameraman;
            private List<String> checklist;
            private List<String> viewer_count;
            private List<String> thumbnail_url;

            public void setEditor(List<String> editor) {
                this.editor = editor;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public void setVideo_link(List<String> video_link) {
                this.video_link = video_link;
            }

            public void setLike(List<String> like) {
                this.like = like;
            }

            public void setIntro(List<String> intro) {
                this.intro = intro;
            }

            public void setFloat_comment(List<String> float_comment) {
                this.float_comment = float_comment;
            }

            public void setWritter(List<String> writter) {
                this.writter = writter;
            }

            public void setCameraman(List<String> cameraman) {
                this.cameraman = cameraman;
            }

            public void setChecklist(List<String> checklist) {
                this.checklist = checklist;
            }

            public void setViewer_count(List<String> viewer_count) {
                this.viewer_count = viewer_count;
            }

            public void setThumbnail_url(List<String> thumbnail_url) {
                this.thumbnail_url = thumbnail_url;
            }

            public List<String> getEditor() {
                return editor;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public List<String> getVideo_link() {
                return video_link;
            }

            public List<String> getLike() {
                return like;
            }

            public List<String> getIntro() {
                return intro;
            }

            public List<String> getFloat_comment() {
                return float_comment;
            }

            public List<String> getWritter() {
                return writter;
            }

            public List<String> getCameraman() {
                return cameraman;
            }

            public List<String> getChecklist() {
                return checklist;
            }

            public List<String> getViewer_count() {
                return viewer_count;
            }

            public List<String> getThumbnail_url() {
                return thumbnail_url;
            }
        }

        public static class TagsEntity {
            /**
             * description :
             * id : 232
             * post_count : 1
             * title : 兵库
             * slug : %e5%85%b5%e5%ba%93
             * group : not assigned
             */
            private String description;
            private int id;
            private int post_count;
            private String title;
            private String slug;
            private String group;

            public void setDescription(String description) {
                this.description = description;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setPost_count(int post_count) {
                this.post_count = post_count;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public String getDescription() {
                return description;
            }

            public int getId() {
                return id;
            }

            public int getPost_count() {
                return post_count;
            }

            public String getTitle() {
                return title;
            }

            public String getSlug() {
                return slug;
            }

            public String getGroup() {
                return group;
            }
        }

        public static class CategoriesEntity {
            /**
             * parent : 0
             * description :
             * id : 2
             * post_count : 57
             * title : Post
             * slug : post
             */
            private int parent;
            private String description;
            private int id;
            private int post_count;
            private String title;
            private String slug;

            public void setParent(int parent) {
                this.parent = parent;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setPost_count(int post_count) {
                this.post_count = post_count;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public int getParent() {
                return parent;
            }

            public String getDescription() {
                return description;
            }

            public int getId() {
                return id;
            }

            public int getPost_count() {
                return post_count;
            }

            public String getTitle() {
                return title;
            }

            public String getSlug() {
                return slug;
            }
        }
    }
}
