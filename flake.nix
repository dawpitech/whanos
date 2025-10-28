{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.11";
    utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, utils }:
    utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
            inherit system;
            config.allowUnfree = true;
        };
      in
      {
        devShells.default = pkgs.mkShell {
          packages = with pkgs; [
            python312
            lazydocker
            dive
            ansible
            kubernetes-helm-wrapped
            kubectl
          ];

          shellHook = ''
            export KUBECONFIG=$PWD/k8s-config.yml
            export ANSIBLE_VAULT_PASSWORD_FILE=/tmp/.vault_pass
          '';
        };

        formatter = pkgs.nixpkgs-fmt;
      });
}
