require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-ed25519-java-bridge"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-ed25519-java-bridge
                   DESC
  s.homepage     = "https://github.com/github_account/react-native-ed25519-java-bridge"
  # brief license entry:
  s.license      = "MIT"
  # optional - use expanded license entry instead:
  # s.license    = { :type => "MIT", :file => "LICENSE" }
  s.authors      = { "Haardik" => "haardik@flexfintx.com" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/flexfintx/react-native-ed25519-java-bridge.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,c,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  # ...
  # s.dependency "..."
end

