#!/bin/bash
# Script to add GitHub topics to Account-Ledger-Library-Kotlin repository
# This script uses GitHub CLI (gh) to add topics

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}GitHub Topics Addition Script${NC}"
echo -e "${BLUE}Repository: Account-Ledger-Library-Kotlin${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if gh is installed
if ! command -v gh &> /dev/null; then
    echo -e "${RED}Error: GitHub CLI (gh) is not installed.${NC}"
    echo "Please install it from: https://cli.github.com/"
    echo ""
    echo "Installation commands:"
    echo "  Ubuntu/Debian: sudo apt install gh"
    echo "  macOS: brew install gh"
    echo "  Windows: winget install --id GitHub.cli"
    exit 1
fi

echo -e "${GREEN}✓ GitHub CLI is installed${NC}"
echo ""

# Check if authenticated
if ! gh auth status &> /dev/null; then
    echo -e "${YELLOW}GitHub CLI is not authenticated.${NC}"
    echo "Running authentication..."
    gh auth login
fi

echo -e "${GREEN}✓ GitHub CLI is authenticated${NC}"
echo ""

# Define topics array
topics=(
    "kotlin"
    "kotlin-library"
    "gradle"
    "gradle-kotlin-dsl"
    "jvm"
    "kotlin-multiplatform"
    "accounting"
    "ledger"
    "finance"
    "transactions"
    "double-entry-bookkeeping"
    "account-management"
    "retrofit"
    "ktor"
    "kotlinx-coroutines"
    "kotlinx-serialization"
    "okhttp"
    "rest-api"
    "api-client"
    "csv"
    "library"
    "financial-data"
    "azure-pipelines"
    "renovate"
    "continuous-integration"
)

echo -e "${BLUE}Topics to be added (${#topics[@]} total):${NC}"
for topic in "${topics[@]}"; do
    echo "  - $topic"
done
echo ""

# Ask for confirmation
read -p "Do you want to proceed with adding these topics? (y/n) " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Operation cancelled.${NC}"
    exit 0
fi

echo ""
echo -e "${BLUE}Adding topics to repository...${NC}"

# Add all topics at once
gh repo edit Baneeishaque/Account-Ledger-Library-Kotlin \
  --add-topic kotlin \
  --add-topic kotlin-library \
  --add-topic gradle \
  --add-topic gradle-kotlin-dsl \
  --add-topic jvm \
  --add-topic kotlin-multiplatform \
  --add-topic accounting \
  --add-topic ledger \
  --add-topic finance \
  --add-topic transactions \
  --add-topic double-entry-bookkeeping \
  --add-topic account-management \
  --add-topic retrofit \
  --add-topic ktor \
  --add-topic kotlinx-coroutines \
  --add-topic kotlinx-serialization \
  --add-topic okhttp \
  --add-topic rest-api \
  --add-topic api-client \
  --add-topic csv \
  --add-topic library \
  --add-topic financial-data \
  --add-topic azure-pipelines \
  --add-topic renovate \
  --add-topic continuous-integration

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Topics added successfully!${NC}"
    echo ""
    echo -e "${BLUE}Verifying topics...${NC}"
    
    # View the topics
    gh repo view Baneeishaque/Account-Ledger-Library-Kotlin --json repositoryTopics -q '.repositoryTopics[].name' | sort
    
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}All topics have been added successfully!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo "You can view the repository at:"
    echo "https://github.com/Baneeishaque/Account-Ledger-Library-Kotlin"
else
    echo ""
    echo -e "${RED}Error: Failed to add topics.${NC}"
    echo "Please check your permissions and try again."
    exit 1
fi
