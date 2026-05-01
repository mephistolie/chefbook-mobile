# ChefBook Mobile Agents Guide

This directory contains the primary mobile client for ChefBook.

## Stack

- Kotlin Multiplatform and Android multi-module project
- Jetpack Compose for UI
- MVI-style presentation architecture
- Koin for dependency injection
- Gradle Kotlin DSL build configuration

## Module Layout

- `android/` contains Android-specific app, UI, navigation, and feature modules.
- `common/` contains shared libraries, SDK modules, features, and UI building blocks.
- `build-logic/` contains convention plugins and shared Gradle build logic.

## Working Rules

- Keep feature work inside the smallest owning module instead of spreading logic across unrelated modules.
- Respect existing separation between `api`, `impl`, `external`, and `internal` modules.
- Do not introduce backend assumptions into presentation code; keep transport and persistence concerns inside SDK or data layers.
- Prefer extending existing patterns for Compose, Flow, and MVI instead of introducing a second architectural style.
- Avoid large dependency graph changes unless the task explicitly requires them.

## Validation

- Use Gradle commands scoped to the touched modules when possible.
- Prefer targeted compilation or tests over full-project rebuilds unless the change is cross-cutting.

## Coordination

- If a task also touches backend contracts, keep protocol or API assumptions explicit and verify the matching backend module.
- For repository-wide context, also read the root `AGENTS.md`.
