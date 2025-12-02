// Script to add GitHub topics to Account-Ledger-Library-Kotlin repository
// This script uses the Octokit library
// 
// Prerequisites:
// npm install @octokit/rest
//
// Usage:
// GITHUB_TOKEN=your_token node add_github_topics.js
//
// Optional environment variables:
// REPO_OWNER - Repository owner (default: Baneeishaque)
// REPO_NAME - Repository name (default: Account-Ledger-Library-Kotlin)

const { Octokit } = require("@octokit/rest");

// Configuration - can be overridden with environment variables
const REPO_OWNER = process.env.REPO_OWNER || "Baneeishaque";
const REPO_NAME = process.env.REPO_NAME || "Account-Ledger-Library-Kotlin";

// Topics to add
const topics = [
  "kotlin", "kotlin-library", "gradle", "gradle-kotlin-dsl", "jvm",
  "kotlin-multiplatform", "accounting", "ledger", "finance", "transactions",
  "double-entry-bookkeeping", "account-management", "retrofit", "ktor",
  "kotlinx-coroutines", "kotlinx-serialization", "okhttp", "rest-api",
  "api-client", "csv", "library", "financial-data", "azure-pipelines",
  "renovate", "continuous-integration"
];

async function main() {
  console.log("=".repeat(50));
  console.log("GitHub Topics Addition Script (Node.js)");
  console.log(`Repository: ${REPO_OWNER}/${REPO_NAME}`);
  console.log("=".repeat(50));
  console.log();

  // Get GitHub token
  const githubToken = process.env.GITHUB_TOKEN;

  if (!githubToken) {
    console.error("Error: GITHUB_TOKEN environment variable is not set.");
    console.log();
    console.log("Please set it using:");
    console.log("  export GITHUB_TOKEN='your_github_personal_access_token'");
    console.log();
    console.log("Or on Windows:");
    console.log("  set GITHUB_TOKEN=your_github_personal_access_token");
    console.log();
    console.log("To create a personal access token:");
    console.log("1. Go to https://github.com/settings/tokens");
    console.log("2. Click 'Generate new token (classic)'");
    console.log("3. Give it a name and select 'repo' scope");
    console.log("4. Click 'Generate token' and copy the token");
    process.exit(1);
  }

  // Initialize Octokit
  const octokit = new Octokit({
    auth: githubToken
  });

  console.log(`Topics to be added (${topics.length} total):`);
  topics.forEach(topic => {
    console.log(`  - ${topic}`);
  });
  console.log();

  // In a real interactive environment, you'd use readline
  // For automation, we'll proceed directly
  console.log("Adding topics to repository...");

  try {
    const response = await octokit.repos.replaceAllTopics({
      owner: REPO_OWNER,
      repo: REPO_NAME,
      names: topics
    });

    console.log();
    console.log("✓ Topics added successfully!");
    console.log();
    console.log("Current topics:");
    response.data.names.sort().forEach(topic => {
      console.log(`  - ${topic}`);
    });
    console.log();
    console.log("=".repeat(50));
    console.log("All topics have been added successfully!");
    console.log("=".repeat(50));
    console.log();
    console.log("You can view the repository at:");
    console.log(`https://github.com/${REPO_OWNER}/${REPO_NAME}`);
  } catch (error) {
    console.error();
    console.error("Error updating topics:", error.message);
    if (error.status) {
      console.error("Status:", error.status);
    }
    process.exit(1);
  }
}

main();
