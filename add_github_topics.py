"""
Script to add GitHub topics to Account-Ledger-Library-Kotlin repository
This script uses the GitHub API to add topics
"""

import requests
import json
import os
import sys

# Configuration
REPO_OWNER = "Baneeishaque"
REPO_NAME = "Account-Ledger-Library-Kotlin"

# Topics to add
topics = [
    "kotlin", "kotlin-library", "gradle", "gradle-kotlin-dsl", "jvm",
    "kotlin-multiplatform", "accounting", "ledger", "finance", "transactions",
    "double-entry-bookkeeping", "account-management", "retrofit", "ktor",
    "kotlinx-coroutines", "kotlinx-serialization", "okhttp", "rest-api",
    "api-client", "csv", "library", "financial-data", "azure-pipelines",
    "renovate", "continuous-integration"
]

def main():
    print("=" * 50)
    print("GitHub Topics Addition Script (Python)")
    print(f"Repository: {REPO_OWNER}/{REPO_NAME}")
    print("=" * 50)
    print()
    
    # Get GitHub token
    github_token = os.environ.get('GITHUB_TOKEN')
    
    if not github_token:
        print("Error: GITHUB_TOKEN environment variable is not set.")
        print()
        print("Please set it using:")
        print("  export GITHUB_TOKEN='your_github_personal_access_token'")
        print()
        print("Or on Windows:")
        print("  set GITHUB_TOKEN=your_github_personal_access_token")
        print()
        print("To create a personal access token:")
        print("1. Go to https://github.com/settings/tokens")
        print("2. Click 'Generate new token (classic)'")
        print("3. Give it a name and select 'repo' scope")
        print("4. Click 'Generate token' and copy the token")
        sys.exit(1)
    
    # API endpoint
    url = f"https://api.github.com/repos/{REPO_OWNER}/{REPO_NAME}/topics"
    
    # Headers
    headers = {
        "Accept": "application/vnd.github.mercy-preview+json",
        "Authorization": f"token {github_token}",
        "Content-Type": "application/json"
    }
    
    print(f"Topics to be added ({len(topics)} total):")
    for topic in topics:
        print(f"  - {topic}")
    print()
    
    # Ask for confirmation
    response = input("Do you want to proceed with adding these topics? (y/n): ")
    if response.lower() != 'y':
        print("Operation cancelled.")
        sys.exit(0)
    
    print()
    print("Adding topics to repository...")
    
    # Update topics
    try:
        response = requests.put(
            url,
            headers=headers,
            data=json.dumps({"names": topics})
        )
        
        if response.status_code == 200:
            print()
            print("✓ Topics added successfully!")
            print()
            print("Current topics:")
            result = response.json()
            for topic in sorted(result.get('names', [])):
                print(f"  - {topic}")
            print()
            print("=" * 50)
            print("All topics have been added successfully!")
            print("=" * 50)
            print()
            print("You can view the repository at:")
            print(f"https://github.com/{REPO_OWNER}/{REPO_NAME}")
        else:
            print()
            print(f"Error: {response.status_code}")
            print(response.text)
            sys.exit(1)
            
    except Exception as e:
        print()
        print(f"Error: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    main()
