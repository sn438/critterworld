Assignment 4
============

Included in this README is information on using Git and Gradle, which are a part of the final project. Please add your regular README information under the README section. Note that Github uses markdown (.md extension) to format its READMEs. More information on markdown here: https://guides.github.com/features/mastering-markdown/

README
============
\\\\TODO- Fill me out!

Distributed Version Control: Git
============================
Distributed Version Control is a system that allows users to track different versions of their code, and to also distribute their code. Git is one example of version control that people use. The motivation is this: You’re happily coding along for A4 and things are working great. Suddenly, you realize that within the last few changes you made, you accidentally introduced a bug. However, you don’t know how many undoes to make in order to have a working assignment again! This is where version control comes in. It lets you take *control* over the different *versions* of your project by keeping a history of all the changes you’ve made. This means you can revert back to previous versions should you make a mistake. In addition, you can push your code to places called repositories that other people can see and get your code from. This makes it very easy to *distribute* your code and collaborate with others. We will be working through Github, a company that hosts projects that use Git version control.

Overall System
============
How does Git get code from your computer to your collaborators? Your code is actually tracked in multiple places at once. We call each of these places a *repository*. For a single project with no dependencies, you will have two repositories:

* The local repository- This is the folder on your computer where you keep your code. When you begin using git, you have to initialize a folder (or directory- they mean the same thing) in order to have git track its files. This creates a .git file in the folder that tells git about your code’s history and what remote repository it is connected to.
There is a kind of staging area in your local repository, which we will discuss with git add.
* The remote repository- In order to distribute code, Github hosts public repositories that are free to use. Much like the local repositories, these remote repositories hold a copy of your code and code history. However, unlike your local repository, you can access remote repositories online through Github’s website.

In order to make progress on your code using distributed version control, you must explicitly save a version using git’s commands. To get changes from your local repository to your corresponding remote repository hosted by Github, you must go through 3 steps/commands:
* `git add` <File1> <File2> <etc>- This command adds your changed files to the staging area mentioned earlier. At the staging area, no official changes have been made: you are only preparing for a full commit at this point. There are many useful options, such as -A, which adds all changed files. More options and detail can be found here: https://git-scm.com/docs/git-add
* `git commit`- This command takes all the changes in your staging area, and commits them to the git history. This is a permanent change to your version history, but this should not scare you as all versions are tracked through version control. However, this does not tell your remote repository that you have made changes on your local computer. The next command will take care of that. In Git, every version is often called a commit, and each commit gets a unique ID that can be used to revert back to that specific version. All git commits are required to have a descriptive message, so the -m <message> flag is quite convenient. See more here: https://git-scm.com/docs/git-commit
* `git push`- This command pushes all your changes to your remote repository, essentially syncing up your remote repository with the changes you made from your local computer. Read more here: https://git-scm.com/docs/git-push

When working with multiple people, there is still only one remote repository. However, each person will have a different local repository. To share your code, you will have to add, commit, and push your changes to the remote repository for the project. In order to get changes that other collaborators have made, you must use two other commands:
* `git fetch`- This fetches the changes from the remote repository, but does not do anything with them. https://git-scm.com/docs/git-fetch
* `git merge`- This takes any changes that were fetched, and attempts to merge those with your version of the code. Say your partner pushed their changes, you would typically fetch those changes, and then merge them in with your code. This may cause merge conflicts, which must be resolved by hand and the final result must be committed.
Because of how often you'll want to merge all the new changes you fetch, there is a command `git pull` that runs both fetch and merge.


Getting Started With Git
=========================
Git installation instructions can be found here: https://git-scm.com/book/id/v2/Getting-Started-Installing-Git
This website also contains official documentation for all git commands, and is very good reference material.
We recommend using Git from terminal.


First Time Setup
================
To get started, you will want to clone your git repository, which we have already set up for you. To do this, look for the green "Clone or download" button on the right. Once you click on this, a url should pop up. Copy this url, and then pull up your terminal and navigate to whatever folder you would like to download your repository to. Then, run the following command:

```
git clone <your url here>
```

You should see your project automatically get downloaded. Navigate into your new local repository using `cd`, and then run the following two commands to set up your profile:

```
git config --global user.name "netIDHere"
git config --global user.email netId@cornell.edu
```

This will set your github username and email so that it knows who to attribute your changes to. For more in depth setup instructions, see the documentation: https://git-scm.com/book/en/v2/Getting-Started-First-Time-Git-Setup

See the project instructions on the course website for information on how to get started with Git and Gradle: http://www.cs.cornell.edu/courses/cs2112/2017fa/project/project.pdf

When adding your files, please only add your src folder, using `git add ./src`. This keeps other unnescessary files generated by gradle, eclipse, and intellij out of your repository.
