// @ts-check

const eslint = require("@eslint/js");
const tseslint = require("typescript-eslint");
const angular = require("angular-eslint");

module.exports = tseslint.config(

  {
    files: ["**/*.ts"],

    extends: [
      eslint.configs.recommended,
      tseslint.configs.recommended,
      angular.configs.tsRecommended
    ],

    processor: angular.processInlineTemplates,

    rules: {

      "@angular-eslint/directive-selector": [
        "error",
        {
          type: "attribute",
          prefix: "app",
          style: "camelCase",
        },
      ],

      "@angular-eslint/component-selector": [
        "error",
        {
          type: "element",
          prefix: "app",
          style: "kebab-case",
        },
      ],


      // Desabilitar regras para o trabalho
      "@typescript-eslint/no-explicit-any": "off",

      "@typescript-eslint/no-empty-function": "off",

      "@typescript-eslint/no-inferrable-types": "off",

      "prefer-const": "off",

      "eqeqeq": 'off'
    },
  },


  {
    files: ["**/*.html"],

    extends: [
      ...angular.configs.templateRecommended,
      ...angular.configs.templateAccessibility,
    ],

    rules: {

      "@angular-eslint/template/click-events-have-key-events": "off",

      "@angular-eslint/template/interactive-supports-focus": "off",

    },
  }

);