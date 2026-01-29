#!/bin/bash
# Add JAVA_HOME to ~/.zshrc if not already present

JAVA_HOME_LINE='export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home'

if grep -q "JAVA_HOME.*jdk-17" ~/.zshrc 2>/dev/null; then
  echo "✓ JAVA_HOME already set in ~/.zshrc"
else
  echo "Adding JAVA_HOME to ~/.zshrc..."
  echo "" >> ~/.zshrc
  echo "# Java 17 for electricity-billing project" >> ~/.zshrc
  echo "$JAVA_HOME_LINE" >> ~/.zshrc
  echo "✓ Added JAVA_HOME to ~/.zshrc"
  echo ""
  echo "Run: source ~/.zshrc"
  echo "Or restart your terminal."
fi
