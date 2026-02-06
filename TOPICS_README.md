# Adding GitHub Topics - Quick Start Guide

This repository includes comprehensive documentation and scripts to add GitHub topics to the Account-Ledger-Library-Kotlin repository.

## Files Included

1. **GITHUB_TOPICS.md** - Complete documentation of recommended topics and all methods to add them
2. **add_github_topics.sh** - Bash script using GitHub CLI (recommended)
3. **add_github_topics.py** - Python script using GitHub API
4. **add_github_topics.js** - Node.js script using Octokit

## Quick Start

### Method 1: Using GitHub CLI (Recommended - Easiest)

```bash
# Make the script executable
chmod +x add_github_topics.sh

# Run the script
./add_github_topics.sh
```

The script will:
- Check if GitHub CLI is installed
- Authenticate if needed
- Show you the topics to be added
- Ask for confirmation
- Add all topics to the repository
- Verify the topics were added

### Method 2: Using Python

```bash
# Install requests library if needed
pip install requests

# Set your GitHub token
export GITHUB_TOKEN='your_github_personal_access_token'

# Run the script
python add_github_topics.py
```

### Method 3: Using Node.js

```bash
# Install dependencies
npm install @octokit/rest

# Set your GitHub token
export GITHUB_TOKEN='your_github_personal_access_token'

# Run the script
node add_github_topics.js
```

### Method 4: Manual via GitHub Web Interface

1. Go to: https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin
2. Click the ⚙️ (settings) icon next to "About" on the right sidebar
3. Add topics one by one from the list in GITHUB_TOPICS.md
4. Click "Save changes"

## Topics to be Added

The following 25 topics will be added:

**Core Technologies:**
- kotlin, kotlin-library, gradle, gradle-kotlin-dsl, jvm, kotlin-multiplatform

**Domain:**
- accounting, ledger, finance, transactions, double-entry-bookkeeping, account-management

**Frameworks & Libraries:**
- retrofit, ktor, kotlinx-coroutines, kotlinx-serialization, okhttp

**Features:**
- rest-api, api-client, csv, library, financial-data

**DevOps:**
- azure-pipelines, renovate, continuous-integration

## Getting a GitHub Personal Access Token

If you need to create a GitHub Personal Access Token:

1. Go to: https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Give it a descriptive name (e.g., "Add Repository Topics")
4. Select the "repo" scope
5. Click "Generate token"
6. Copy the token immediately (you won't be able to see it again)

## Verification

After adding topics, verify them by:

```bash
# Using GitHub CLI
gh repo view Baneeishaque/Account-Ledger-Library-Kotlin --json repositoryTopics

# Or visit the repository in your browser
```

## More Information

See **GITHUB_TOPICS.md** for:
- Detailed explanation of each topic
- Alternative methods to add topics
- API usage examples
- Benefits of adding topics
- Topic selection rationale

## Support

If you encounter any issues:
1. Check that you have the required permissions (owner or admin access)
2. Ensure your GitHub token has the "repo" scope
3. Verify you're authenticated with GitHub CLI
4. Review the error messages for specific issues

For more details, see the comprehensive documentation in GITHUB_TOPICS.md.
