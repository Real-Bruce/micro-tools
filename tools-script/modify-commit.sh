git filter-branch --commit-filter '
if [ "$GIT_AUTHOR_NAME" = "target_author_name" ];
then
        GIT_AUTHOR_NAME="new_author_name";
        GIT_AUTHOR_EMAIL="new_author_email";
        git commit-tree "$@";
else
        git commit-tree "$@";
fi' HEAD