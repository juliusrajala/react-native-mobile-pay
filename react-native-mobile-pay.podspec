require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-mobile-pay"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-mobile-pay
                   DESC
  s.homepage     = "https://github.com/github_account/react-native-mobile-pay"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Julius Rajala" => "julius@identio.fi" }
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/identiofi/react-native-mobile-pay.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  # ...
  # s.dependency "..."
end

